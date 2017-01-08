(function(web){


    web.init = function init() {

        function now() {
            var now = new Date();
            var month = now.getMonth() + 1;
            if(month < 10) {
                month = "0" + month;
            }
            var day = now.getDate();
            if(day < 10) {
                day = "0" + day;
            }
            return [now.getFullYear(), month, day].join('-');
        }

        var self = this;
        if(self.started) {
            return;
        }

        // Navigation
        self.activateNavigationLink("home");

        // Default date
        self.jquery("#date").val(now());

        self.notebooks = [];
        self.tags = [];

        // Load categories
        self.log("Loading notebooks.");
        self.get("/api/v1/notebooks", {}, function(data) {
            data = data.content;
            self.log("Retrieved " + data.length + " notebooks.");
            var selector = self.jquery("#notebook");

            // TODO: Validation
            self.jquery.each(data, function(index, notebook) {
                self.notebooks.push(notebook);
                selector.append(self.jquery("<option>", {value: notebook.code, text: notebook.name}));
            });

            selector.prop("selectedIndex", -1);

        }).fail(function(){
            self.error("Error requesting notebooks");
        });

        // Load tags
        self.log("Loading tags.");
        self.get("/api/v1/tags", {}, function(data){
            self.log("Retrieved " + data.length + " tags.");
            var selector = self.jquery("#tags");

            // TODO: Validation
            self.jquery.each(data, function(index, tag) {
                self.tags.push(tag);
                selector.append(self.jquery("<option>", {value: tag.code, text: tag.code}));
            });

            selector.prop("selectedIndex", -1);

            self.jquery("#tags").select2({tags: true});

        }).fail(function(){
            console.error("Error requesting tags");
            self.displayError("Error requesting tags to the server. Please retry it.");
        });

        self.started = true;

    };

    web.register();

})(window.web);