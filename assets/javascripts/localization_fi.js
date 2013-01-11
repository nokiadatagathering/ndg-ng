var LOC = (function() {

    var constants = {
        LOC_CSV: 'Lataa CSV-tiedostomuodossa',
        LOC_XLS: 'Lataa XLS-tiedostomuodossa',
        LOC_CLOSE: 'Sulje',
        LOC_EXPORT_IMAGES: 'Valitsemassasi lomakkeessa on kuvia vastauksina. Haluatko viedä myös kuvat?',
        LOC_EXPORT_FORMAT: 'Valitse tiedostomuoto vastausten viemiseksi',
        LOC_YES: 'Kyllä',
        LOC_NO: 'Ei',
        LOC_RESULTID: 'Vastauksen ID',
        LOC_RESULTTITLE: 'Vastauksen otsikko',
        LOC_DATESENT: 'Lähettämispäivämäärä',
        LOC_USER: 'Käyttäjä',
        LOC_LOCATION: 'Sijainti',
        LOC_BACK_TO_SURVEY_LIST: 'Palaa lomakelistalle',
        LOC_EXPORT_RESULTS: 'Vie vastaukset',
        LOC_EXPORT_ALL_RESULTS: 'Vie kaikki vastaukset',
        LOC_DOWNLOAD: 'Lataa',
        LOC_UPLOAD: 'Siirrä',
        LOC_EDIT: 'Editoi',
        LOC_DUPLICATE: 'Kopioi',
        LOC_DELETE: 'Poista',
        LOC_SEND: 'Lähetä',
        LOC_PREVIEW: 'Preview',
        LOC_SURVEY_NAME: 'Lomakkeen nimi',
        LOC_DATE_PUBLISHED: 'PVM.',
        LOC_PUBLISHER: 'Lupa',
        LOC_RESULTS: 'Vastaukset',
        LOC_SURVEYID: 'Lomakkeen ID',
        LOC_NEW_SURVEY: 'Uusi lomake',
        LOC_CHECK: 'Tarkista',
        LOC_NAME: 'Nimi',
        LOC_PHONE: 'Puhelinnumero',
        LOC_EMAIL: 'Sähköpostiosoite',
        LOC_PERMISSION: 'Oikeudet',
        LOC_ALL: 'Kaikki',
        LOC_ALL_PAGES: 'Kaikki sivut',
        LOC_NONE: 'Ei yhtään',
        LOC_SURVEY_DELETE_CONFIRM: 'Haluatko varmasti poistaa lomakkeen?',
        LOC_CHOOSE_SURVEY_UPLOAD: 'Valitse siirrettävä lomake',
        LOC_SURVEY_UPLOAD: 'Lomakkeen siirto',
        LOC_SEND_FILE: 'Lähetä tiedosto',
        LOC_SEND_SURVEY: 'Lähetä lomake',
        LOC_DONE: 'Tehty',
        LOC_SEARCH: 'Etsi...',
        LOC_USERNAME: 'Käyttäjätunnus',
        LOC_CONFIRM_DELETE: 'Käyttäjän poistaminen poistaa kaikki oheiset lomakkeet ja vastaukset! Klikkaa jatkaaksesi.',
        LOC_NEW_USER: 'Uusi käyttäjä',
        LOC_GROUP: 'Ryhmä',
        LOC_USERS: 'Käyttäjä(t)',
        LOC_NEW_GROUP: 'Uusi ryhmä',
        LOC_FIRST_NAME: 'Etunimi',
        LOC_LAST_NAME: 'Sukunimi',
        LOC_PASSWORD: 'Salasana',
        LOC_RETYPE_PASSWORD: 'Toista salasana',
        LOC_PHONE_NUMBER_LONG: 'Puhelinnumero',
        LOC_ADMIN: 'Järjestelmänvalvoja',
        LOC_FIELD_WORKER: 'Kenttätyöntekijä',
        LOC_OPERATOR: 'Operaattori',
        string: 'Kuvaileva',
        'int': 'Kokonaisluku',
        decimal: 'Desimaali',
        date: 'Päivämäärä',
        'binary#image': 'Kuva',
        select: 'Monivalinnainen',
        select1: 'Yksinomainen',
        time: 'Aika',
        geopoint: 'Location',
        LOC_SAVESURVEY:'Tallenna lomake',
        LOC_CANCEL: 'Peruuta',
        LOC_RESULT_LIST: 'Vastauslista',
        LOC_MAP_VIEW: 'Näytä kartalla',
        LOC_GRAPH_VIEW: 'Näytä kaaviona',
        LOC_PIE_CHART: 'Ympyräkaavio',
        LOC_BAR_CHART: 'Pylväsdiagrammi',
        LOC_EXPORT_TO: 'Vie',
        LOC_SCHEDULE_EXPORT: 'Schedule',
        LOC_SCHEDULE_DESC: 'All results between the two dates will be exported from the server. They will then be emailed to the address entered on the form below',
        LOC_SCHEDULE_DATEFROM: 'Date From',
        LOC_SCHEDULE_DATETO: 'Date To',
        LOC_SCHEDULE_EMAILTO: 'Email To',
        LOC_SEND_SMS: 'Lähetä SMS',
        LOC_TO: 'Mihin',
        LOC_PHONE_NUMBER: 'Puhelinnumero',
        LOC_EXTERNAL_SERVICE: 'Ulkopuolinen palvelu',
        LOC_CANNOT_EDIT_SURVEY: 'Lomaketta ei voi muokata. Lomake on jo saatavissa. Kopioi lomake ensin ja sitten muokkaa se.',
        LOC_SURVEY_LIST: 'Lomakelista',
        LOC_USER_ADMIN: 'Käyttäjienhallinta',
        LOC_NEW_CATEGORY: 'Uusi kategoria',
        LOC_NEW_QUESTION: 'Uusi kysymys',
        LOC_NEW_OPTION: 'Uusi vaihtoehto',
        LOC_SAVE_SURVEY : 'Haluatko tallentaa lomakkeen?',
        LOC_LENGTH : 'Maksimipituus',
        LOC_MIN_RANGE: 'Maksimivaihteluväli ',
        LOC_MAX_RANGE: 'Minimivaihteluväli',
        LOC_REQUIRED: 'REQUIRED',
        LOC_BACK_TO_SURVEY_LIST: 'Go back to survey list',
        LOC_DRAG_NEW_CATEGORY: 'Vedä lisätäksesi uuden kategorian',
        LOC_DRAG_NEW_QUESTION : 'Vedä lisätäksesi uuden kysymyksen',
        LOC_DROP_CATEGORY: 'Pudota tähän lisätäksesi uuden kategorian',
        LOC_DROP_QUESTION: 'Pudota tähän lisätäksesi uuden kysymyksen',
        LOC_WARN_DELETE_OPTION :'Tämän kysymystyypin täytyy sisällyttää ainakin yksi vaihtoehto',
        LOC_RESULT_DELETE_CONFIRM: 'Haluatko varmasti poistaa vastauksen?',
        LOC_MSG_PASSWORD_NOT_MATCH: 'Virheellinen salasana',
        LOC_MSG_SHORT_NUMBER: 'Liian lyhyt puhelinnumero',
        LOC_EXPAND_ITEM_LIST:'Klikkaa tästä nähdäksesi lisää kohtia',
        LOC_LOADING: 'Ladataan',
        LOC_BUILDING: 'Rakennetaan',
        LOC_AVAILABLE: 'Saatavissa',
        LOC_CLOSED: 'Suljettu',
        LOC_STATUS: 'STATUS',
        LOC_CSV_FILE_UPLOAD: 'Ladataan CSV-tiedosto',
        LOC_CHOOSE_CSV_FILE_UPLOAD: 'Valitse CSV-tiedosto ladattavaksi',
        LOC_LOAD: 'Lataa',
        LOC_CLICK_TO_DISABLE_FILTER: 'Klikkaa tästä poistaaksesi suodattimen',
        LOC_CLICK_TO_DISABLE_SELECTED_GROUP: 'Vedä ja pudota käyttäjiä tai ryhmiä'
    };

    return {
        get: function(name) {
            return constants[name];
        }
    };
})();

