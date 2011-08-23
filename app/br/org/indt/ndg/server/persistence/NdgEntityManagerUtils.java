/*
 *  Copyright (C) 2010-2011  INdT - Instituto Nokia de Tecnologia
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
package br.org.indt.ndg.server.persistence;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

import play.db.jpa.JPA;

/**
 *
 * @author wojciech.luczkow
 */
public class NdgEntityManagerUtils {

    public static boolean remove( Object object ) {
        EntityManager em = JPA.em();
        boolean retval = true;
        try {
//            em.getTransaction().begin();
            em.remove( object );
//            em.getTransaction().commit();
            retval = true;
        } catch ( Exception e ) {
            Logger.getAnonymousLogger().log( Level.SEVERE, "remove exception caught", e );
//            em.getTransaction().rollback();
        } 
        return retval;
    }

    public static boolean persist( Object object ) {
        EntityManager em = JPA.em();
        boolean retval = true;
        try {
//            em.getTransaction().begin();
            em.persist( object );
//            em.getTransaction().commit();
            retval = true;
        } catch ( Exception e ) {
            e.printStackTrace();
            Logger.getAnonymousLogger().log( Level.SEVERE, "persist exception caught", e );
//            em.getTransaction().rollback();
        }
        return retval;
    }

    public static boolean persist( EntityManager em, Object object ) {
        boolean retval = true;
        try {
//            em.getTransaction().begin();
            em.persist( object );
//            em.getTransaction().commit();
            retval = true;
        } catch ( Exception e ) {
            Logger.getAnonymousLogger().log( Level.SEVERE, "exception caught", e );
//            em.getTransaction().rollback();
            retval = false;
        }
        return retval;
    }
}
