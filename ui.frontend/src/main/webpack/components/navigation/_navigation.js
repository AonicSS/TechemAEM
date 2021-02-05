(function () {
    "use strict";

    function NotificationModule(el) {
        el.dataset.initialized = true;

        this.$el = $(el);

        this.selectors = {
            headerDropdown: '.header__dropdown-content',
            navigationLink: '.navigation__link',
            closeButton: '.close',
            headerButton: ".header__button",
            active: "navigation-active",
            hidden: 'header__dropdown-hidden'
        };

        this.atributes = {
            dropdownItem: "dropdown-item",
            link: "button-link",
            content: "content",
            protocol: "https://"
        };

        this.init = function () {
            this.showAndHideDropdown();
            this.closeDropDown();
            this.redirectButton();
        }.bind(this);

        this.showAndHideDropdown = function () {
            var $links = this.$el.find(this.selectors.navigationLink);

            $links.on('click', (event) => {
                var $target = $(event.target);
                var $link = undefined;
                $('body').addClass('scroll-hidden');

                if ($target.is("li")) {
                    $link = $target;
                } else {
                    $link = $target.parent();
                }

                var navItemAttr = $link && $link.attr(this.atributes.dropdownItem);

                if (navItemAttr.trim().length > 0) {
                    var $dropdownItem = this.$el.find("div[name='" + navItemAttr + "']");

                    if ($dropdownItem && $dropdownItem.length > 0) {
                        this.hideAllDropDowns();
                        this.removeActive();
                        $dropdownItem.removeClass(this.selectors.hidden);
                        $link.addClass(this.selectors.active);
                    }
                }
            });
        }.bind(this);

        this.closeDropDown = function () {
            var $closeButton = this.$el.find(this.selectors.closeButton);

            $closeButton.on('click', () => {
                this.hideAllDropDowns();
                $('body').removeClass('scroll-hidden');
            });
        }.bind(this);

        this.hideAllDropDowns = function () {
            var $dropdownItems = this.$el.find(this.selectors.headerDropdown);
            if ($dropdownItems && $dropdownItems.length > 0) {
                $dropdownItems.addClass(this.selectors.hidden);
            }
        }.bind(this);

        this.removeActive = function () {
            var $activeItems = this.$el.find(this.selectors.navigationLink);
            if ($activeItems && $activeItems.length > 0) {
                $activeItems.removeClass(this.selectors.active);
            }
        }.bind(this);

        this.redirectButton = function () {
            var $headerButton = this.$el.find(this.selectors.headerButton);

            $headerButton.on('click', () => {
                var redirectLink = $headerButton && $headerButton.attr(this.atributes.link);

                if (redirectLink.includes(this.atributes.content)) {
                    window.location.href = window.location.origin + redirectLink;
                } else {
                    window.location.href = redirectLink.includes(this.atributes.protocol) ?
                        redirectLink : this.atributes.protocol + redirectLink;
                }
            });

        }.bind(this);

        this.init();

        return this;
    }
    /* ---------------------------------------------
    search
    --------------------------------------------- */

    $('.header__search-icon').mouseover(function () {
        $(".header__search-icon").css("display", "none");
        $(".cmp-search").show("slow");
    });
    $('.cmp-search').mouseout(function () {
        if (!$('.cmp-search__input').val() || $('.cmp-search__input').val() === "Search") {
            $(".cmp-search").hide();
            $(".header__search-icon").css("display", "block");
        }
    });
    /* ---------------------------------------------
    scroll shrink
    --------------------------------------------- */
    const body = document.body;
    const scrollUp = "scroll-up";
    const scrollDown = "scroll-down";
    let lastScroll = 0;


    window.addEventListener("scroll", () => {
        const currentScroll = window.pageYOffset;
        if (currentScroll <= 0) {
            body.classList.remove(scrollUp);
            return;
        }

        if (currentScroll > lastScroll && !body.classList.contains(scrollDown)) {
            // down
            body.classList.remove(scrollUp);
            body.classList.add(scrollDown);
        } else if (currentScroll < lastScroll && body.classList.contains(scrollDown)) {
            // up
            body.classList.remove(scrollDown);
            body.classList.add(scrollUp);
        }
        lastScroll = currentScroll;
    });
    /* ---------------------------------------------
    burger menu
    --------------------------------------------- */
    function burgermenuinit() {
        var clickDelay = 500,
            clickDelayTimer = null;
        $('.burger-click-region').on('click', function () {
            if (clickDelayTimer === null) {
                var $burger = $(this);
                $('body').css('overflow', 'hidden');
                $burger.toggleClass('active');
                $burger.parent().toggleClass('is-open');
                $('nav.navigation').slideDown();

                if (!$burger.hasClass('active')) {
                    $burger.addClass('closing');
                    $('body').removeAttr( 'style' );
                    $('nav.navigation').slideUp();
                }
                clickDelayTimer = setTimeout(function () {
                    $burger.removeClass('closing');
                    clearTimeout(clickDelayTimer);
                    clickDelayTimer = null;
                }, clickDelay);
            }
        });
    }
    $(document).ready(function () {
        var $notification = $("header[data-component-name='navigation']");
        Array.prototype.forEach.call($notification, function (element) {
            if (element && element.dataset.initialized !== "true") {
                new NotificationModule(element);
            }
        });
        burgermenuinit();
    });
}());