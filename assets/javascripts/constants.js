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