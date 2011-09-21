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
        LOC_RESULTID: 'ResultId',
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
        LOC_NEW_SURVEY: 'New Survey'
    };

    var constants_POL = {
        LOC_CSV: 'Sci1gnij w formacie CSV',
        LOC_XLS: 'Oci1gnij w fgormacie XLS',
        LOC_CLOSE: 'Zamknij',
        LOC_EXPORT_IMAGES: 'Rezultaty tej ankiety zawieraj1 zdjecia. Czy do31czyc je do wyników?',
        LOC_EXPORT_FORMAT: 'Wybierz format pliku z wynikami',
        LOC_YES: 'Tak',
        LOC_NO: 'Nie',
        LOC_RESULTID: 'ID Resultat',
        LOC_RESULTTITLE: 'Tytul',
        LOC_DATESENT: 'Data wys3ania',
        LOC_USER: 'Uzytkownik',
        LOC_LOCATION: 'Lokalizacja',
        LOC_BACK_TO_SURVEY_LIST: 'Wróc do list ankiet',
        LOC_EXPORT_RESULTS: 'Exportuj resultaty',
        LOC_EXPORT_ALL_RESULTS: 'Export wszytkie resultaty',
        LOC_DOWNLOAD: 'Oci1gnij',
        LOC_UPLOAD: 'Za3aduj',
        LOC_EDIT: 'Edituj',
        LOC_DUPLICATE: 'Duplikuj',
        LOC_DELETE: 'Skasuj',
        LOC_SEND: 'Wyolij',
        LOC_SURVEY_NAME: 'Nazwa ankiety',
        LOC_DATE_PUBLISHED: 'Data publikacji',
        LOC_PUBLISHER: 'Autor',
        LOC_RESULTS: 'Resultaty',
        LOC_SURVEYID: 'ID ankiety',
        LOC_NEW_SURVEY: 'Nowa ankieta'
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

