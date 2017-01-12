(function(web){


    web.init = function init() {

        var self = this;
        if(self.started) {
            return;
        }

        // Navigation
        self.activateNavigationLink("notebooks");

        self.started = true;

    };

    web.register();

})(window.web);