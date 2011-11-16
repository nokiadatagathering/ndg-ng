/*
*  Copyright (C) 2010  INdT - Instituto Nokia de Tecnologia
*
*  NDG is free software; you can redistribute it and/or
*  modify it under the terms of the GNU Lesser General Public
*  License as published by the Free Software Foundation; either
*  version 2.1 of the License, or (at your option) any later version.
*
*  NDG is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*  Lesser General Public License for more details.
*
*  You should have received a copy of the GNU Lesser General Public
*  License along with NDG.  If not, see <http://www.gnu.org/licenses/
*/

package controllers.sms;

import controllers.exceptions.SMSSenderException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.smslib.AGateway;
import org.smslib.GatewayException;
import org.smslib.IOutboundMessageNotification;
import org.smslib.OutboundMessage;
import org.smslib.SMSLibException;
import org.smslib.TimeoutException;
import org.smslib.Message.MessageEncodings;
import org.smslib.modem.SerialModemGateway;
import br.org.indt.smslib.GatewayFactory;
import br.org.indt.smslib.SMSService;
import controllers.exceptions.ModemNotRepondingException;
import controllers.util.PropertiesUtil;

public class SMSModemHandler {

    public static final int SMS_NDG_PORT = 50001;
    public static final int SMS_NORMAL_PORT = 0;

    private static final Logger log = Logger.getLogger("smslog");
    private static final String MODEM_MANUFACTURE = "modem.manufacture";
    private static final String MODEM_MODEL = "modem.model";
    private static final String MODEM_IMEI = "modem.imei";
    private static final String MODEM_SIGNAL_LEVEL = "modem.signal.level";
    private static final String MODEM_BATERY_LEVEL = "modem.batery.level";
    private static final String MODEM_IS_STARTED = "modem.is.started";

    protected static SMSService srv;

    private static SMSModemHandler smsHandle;
    private OutboundNotification outboundNotification;
    private Properties modemProperties, settingsProperties;
    private String sendGatewayId;
    private int rxtx = 0;

    protected SerialModemGateway modemGateway = null;
    protected AGateway brokerGateway = null;

    /**
     *
     */
    protected SMSModemHandler() {
        super();
        settingsProperties = PropertiesUtil.getSettingsProperties();
        modemProperties = PropertiesUtil.getCoreProperties();

        if (modemProperties.getProperty("SMS_MODEM_SEND").equalsIgnoreCase("true")) {
            rxtx = 2;
        }

        if (modemProperties.getProperty("SMS_BROKER_SEND").equalsIgnoreCase("true")) {
            rxtx = 8;
        }

        boolean useProxy = settingsProperties.getProperty("proxy.set").equals("true");

        if (useProxy) {
            System.getProperties().put("proxySet", settingsProperties.getProperty("proxy.set"));
            System.getProperties().put("proxyHost", settingsProperties.getProperty("proxy.host"));
            System.getProperties().put("proxyPort", settingsProperties.getProperty("proxy.port"));
        }

        outboundNotification = new OutboundNotification();
        srv = SMSService.getSmsService();
        setGateways();
        try {
            System.out.println(">>>>> Starting Gateways ...");
            srv.startService();
            log.info(">>>>> ModemGateway: " + modemGateway);
            if (modemGateway != null) {
                log.info(">>>>> ModemGateway (in): " + modemGateway);

                log.info("************** Modem Information ************ ");
                log.info("******** -  Manufacturer: "
                        + modemGateway.getManufacturer());
                log.info("******** -  Model: " + modemGateway.getModel());
                log
                        .info("******** -  Serial No: "
                                + modemGateway.getSerialNo());
                log.info("******** -  SIM IMSI: " + modemGateway.getImsi());
                log.info("******** -  Signal Level: "
                        + modemGateway.getSignalLevel() + "%");
                log.info("******** -  Battery Level: "
                        + modemGateway.getBatteryLevel() + "%");
                log.info("***********************************************");
            }
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (GatewayException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SMSLibException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void setGateways() {
        brokerGateway = GatewayFactory.createNewClickatellGateway(
                settingsProperties.getProperty("sms.gateway.id"),
                settingsProperties.getProperty("sms.api.id"),
                settingsProperties.getProperty("sms.username"),
                settingsProperties.getProperty("sms.password"));

        modemGateway = (SerialModemGateway) GatewayFactory
                .createNewSerialModemGateway(modemProperties
                        .getProperty("MODEM_ID"), modemProperties
                        .getProperty("MODEM_COM_PORT"), new Integer(
                        modemProperties.getProperty("MODEM_BAUD_RATE"))
                        .intValue(), modemProperties
                        .getProperty("MODEM_MANUFACTURER"), modemProperties
                        .getProperty("MODEM_MODEL"));
        modemGateway.setSimPin("0000");

        switch (rxtx) {
        case 2:
            modemGateway.setOutbound(true);
            modemGateway.setOutboundNotification(outboundNotification);
            srv.addGateway(modemGateway);
            sendGatewayId = modemGateway.getGatewayId();
            brokerGateway = null;
            break;
        case 8:
            brokerGateway.setOutbound(true);
            brokerGateway.setOutboundNotification(outboundNotification);
            srv.addGateway(brokerGateway);
            sendGatewayId = brokerGateway.getGatewayId();
            modemGateway = null;
            break;
        }
    }

    public static SMSModemHandler getInstance() {
        if (smsHandle == null) {
            synchronized (SMSModemHandler.class) {
                if (smsHandle == null) {
                    smsHandle = new SMSModemHandler();
                }
            }
        }
        return smsHandle;
    }

    public void sendTextSMS(String dest, String textMsg, int port) throws SMSSenderException {
        if (dest.substring(0, 2).equals("+0")) {
            dest = "+55" + dest.substring(2);
        }
        OutboundMessage msg = new OutboundMessage(dest, textMsg);
        msg.setEncoding(MessageEncodings.ENCUCS2);
        if (sendGatewayId != null) {
            srv.queueMessage(msg, sendGatewayId);
        } else {
            throw new SMSSenderException("There is no SMS sender Gateway configured...");
        }
    }

    public Properties getModemProperties() throws ModemNotRepondingException {
        Properties modemProperties = new Properties();
        try {
            modemProperties.setProperty(MODEM_MANUFACTURE, modemGateway
                    .getManufacturer());
            modemProperties.setProperty(MODEM_MODEL, modemGateway.getModel());
            modemProperties.setProperty(MODEM_IMEI, modemGateway.getSerialNo());
            modemProperties.setProperty(MODEM_BATERY_LEVEL, Integer
                    .toString(modemGateway.getBatteryLevel())
                    + "%");
            modemProperties.setProperty(MODEM_SIGNAL_LEVEL, Integer
                    .toString(modemGateway.getSignalLevel())
                    + "%");
            modemProperties.setProperty(MODEM_IS_STARTED, modemGateway
                    .getStarted() ? "YES" : "NOT");
        } catch (TimeoutException e) {
            e.printStackTrace();
            throw new ModemNotRepondingException(e);
        } catch (GatewayException e) {
            e.printStackTrace();
            throw new ModemNotRepondingException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ModemNotRepondingException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new ModemNotRepondingException(e);
        }
        return modemProperties;
    }

    public class OutboundNotification implements IOutboundMessageNotification {
        public void process(String gatewayId, OutboundMessage msg) {
            System.out.println("Outbound handler called from Gateway: "
                    + gatewayId);
            System.out.println(msg);
        }
    }
}
