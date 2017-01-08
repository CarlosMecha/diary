(function(web){

    web.init = function init() {
        var self = this;

        // Navigation
        self.activateNavigationLink("notebooks");

        // Page links
        self.jquery(".pageLink").each(function(i) {
            var elem = self.jquery(this);
            var ref = elem.attr("href");
            var link = self.jquery("<a></a>").attr({href: ref}).text(elem.text());
            elem.text(""); // Remove text.
            elem.append(link);
        });

        self.started = true;
    }

    web.register();

})(window.web);