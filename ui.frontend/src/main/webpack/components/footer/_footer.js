(function($) {
    "use strict";

    function FooterModule(el) {
            el.dataset.initialized = true;

            this.$el = $(el);

            this.selectors = {
                  category: '.cmp-footer__category'
            };

            this.attributes = {
                category: 'cmp-footer__category',
                active: 'active'
            }

            this.init = function() {
                this.toggleActive();
            }.bind(this);

            this.toggleActive = function() {
                var $category = this.$el.find(this.selectors.category);

                $category.on('click', (event) => {
                     var $target = $(event.target);
                     var $category = undefined;

                     if($target.hasClass(this.attributes.category)) {
                           $category = $target;
                     } else {
                           $category = $target.parent();
                     }

                     $category && $category.toggleClass(this.attributes.active);
                });
            }.bind(this);

            this.init();

            return this;
    }

    $(document).ready(function() {
        var $footer = $("footer[data-component-name='footer']");
        Array.prototype.forEach.call($footer, function(element) {
            if (element && element.dataset.initialized !== "true") {
                new FooterModule(element);
            }
        });
    });
}(jQuery));

