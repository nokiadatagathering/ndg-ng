var CONST = (function() {
    var constants = {
        'ASC' : ' \u2191',
        'DESC' : ' \u2193',
        'TABLE_ROW_COUNT' : 10,
        'TABLE_ROW_HEIGHT_TOTAL' : 45
    };
    return {
        get: function(name) { return constants[name]; }
    };
})();

var QuestionType = { DESCRIPTIVE : 1, INTEGER : 2, DECIMAL : 3, DATE : 4, IMAGE : 6, EXCLUSIVE : 10, CHOICE : 11, TIME : 12}