var LOC = (function() {
    var constants = {
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
        LOC_SURVEYID: 'SurveyId'
    };
    return {
        get: function(name) { return constants[name]; }
    };
})();

