var CONST = (function() {
    var constants = {
        'ASC' : ' \u2191',
        'DESC' : ' \u2193'
    };
    return {
        get: function(name) { return constants[name]; }
    };
})();