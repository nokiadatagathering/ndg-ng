var LOC = (function() {

    var constants;

    var constants_EN = {
        LOC_CSV: 'Download in CSV format',
        LOC_XLS: 'Download in XLS format',
        LOC_CLOSE: 'Close',
        LOC_EXPORT_IMAGES: 'The survey you want to export has images in its results. Do you want to export the images as well?',
        LOC_EXPORT_FORMAT: 'Please, select below the file format to export results.',
        LOC_YES: 'Yes',
        LOC_NO: 'No',
        LOC_RESULTID: 'Result Id',
        LOC_RESULTTITLE: 'Result Title',
        LOC_DATESENT: 'Date Sent',
        LOC_USER: 'User',
        LOC_LOCATION: 'Location',
        LOC_BACK_TO_SURVEY_LIST: 'Back To SurveyList',
        LOC_EXPORT_RESULTS: 'Export Results',
        LOC_EXPORT_ALL_RESULTS: 'Export All Results',
        LOC_DOWNLOAD: 'Download',
        LOC_UPLOAD: 'Upload',
        LOC_EDIT: 'Edit',
        LOC_DUPLICATE: 'Duplicate',
        LOC_DELETE: 'Delete',
        LOC_SEND: 'Send',
        LOC_SURVEY_NAME: 'Survey Name',
        LOC_DATE_PUBLISHED: 'Date Published',
        LOC_PUBLISHER: 'Publisher',
        LOC_RESULTS: 'Results',
        LOC_SURVEYID: 'SurveyId',
        LOC_NEW_SURVEY: 'New Survey',
        LOC_CHECK: 'Check',
        LOC_NAME: 'NAME',
        LOC_PHONE: 'PHONE',
        LOC_EMAIL: 'E-MAIL',
        LOC_PERMISSION: 'PERMISSION',
        LOC_ALL: 'All',
        LOC_ALL_PAGES: 'All Pages',
        LOC_NONE: 'None',
        LOC_SURVEY_DELETE_CONFIRM: 'Are you sure you want to delete survey?',
        LOC_CHOOSE_SURVEY_UPLOAD: 'Choose a survey to upload',
        LOC_SURVEY_UPLOAD: 'Survey upload',
        LOC_SEND_FILE: 'Send File',
        LOC_SEND_SURVEY: 'Send Survey',
        LOC_DONE: 'Done'
    };

    var constants_POL = {
        LOC_CSV: '\u015ci\u0105gnij w formacie CSV',
        LOC_XLS: '\u015aaci\u0105gnij w formacie XLS',
        LOC_CLOSE: 'Zamknij',
        LOC_EXPORT_IMAGES: 'Rezultaty tej ankiety zawieraj\u0105 zdj\u0119cia. Czy do\u0142\u0105czyc je do wyników?',
        LOC_EXPORT_FORMAT: 'Wybierz format pliku z wynikami',
        LOC_YES: 'Tak',
        LOC_NO: 'Nie',
        LOC_RESULTID: 'ID Rezultatu',
        LOC_RESULTTITLE: 'Tytu\u0142',
        LOC_DATESENT: 'Data wys\u0142ania',
        LOC_USER: 'U\u017cytkownik',
        LOC_LOCATION: 'Lokalizacja',
        LOC_BACK_TO_SURVEY_LIST: 'Wróc do list ankiet',
        LOC_EXPORT_RESULTS: 'Exportuj resultaty',
        LOC_EXPORT_ALL_RESULTS: 'Export wszytkie resultaty',
        LOC_DOWNLOAD: '\u015aaci\u0105gnij',
        LOC_UPLOAD: 'Za\u0142aduj',
        LOC_EDIT: 'Edituj',
        LOC_DUPLICATE: 'Duplikuj',
        LOC_DELETE: 'Skasuj',
        LOC_SEND: 'Wy\u015blij',
        LOC_SURVEY_NAME: 'Nazwa ankiety',
        LOC_DATE_PUBLISHED: 'Data publikacji',
        LOC_PUBLISHER: 'Autor',
        LOC_RESULTS: 'Rezultaty',
        LOC_SURVEYID: 'ID ankiety',
        LOC_NEW_SURVEY: 'Nowa ankieta',
        LOC_CHECK: 'Zaznacz',
        LOC_NAME: 'IMI\u0118',
        LOC_PHONE: 'TEL. NUMER',
        LOC_EMAIL: 'E-MAIL',
        LOC_PERMISSION: 'UPRAWNIENIA',
        LOC_ALL: 'Widoczne',
        LOC_ALL_PAGES: 'Wszystkie',
        LOC_NONE: 'Odznacz',
        LOC_SURVEY_DELETE_CONFIRM: 'Czy napewno chcech usun\u0105\u0107 ankiet\u0119?',
        LOC_CHOOSE_SURVEY_UPLOAD: 'Wybierz plik ankiety do za\u0142adowania',
        LOC_SURVEY_UPLOAD: '\u0141aduj ankiet\u0119',
        LOC_SEND_FILE: 'Wy\u015blij',
        LOC_SEND_SURVEY: 'Wy\u015blij ankiet\u0119',
        LOC_DONE: 'Wy\u015blij'
    };


     function selectLanguage(languageName) {
        if( languageName == 'flag_pol' ) {
            constants = constants_POL;
        } else if ( languageName == 'flag_bra' ){
            constants = constants_BRA;
        } else if ( languageName == 'flag_spa' ) {
            constants = constants_SPA;
        } else {
            constants = constants_EN;
        }
        window.name= languageName;
    }



    return {
        get: function(name) {
            if( window.name == "" ) {
                window.name='flag_eua';
            }
            selectLanguage(window.name);
            return constants[name];

    },
        selectLanguage: function(event) {selectLanguage(event.data)}
    };
})();

