(function () {
    "use strict";

    function NotificationModule(el) {
        el.dataset.initialized = true;

        this.$el = $(el);

        this.selectors = {
            headerDropdown: '.header__dropdown-content',
            navigationLink: '.navigation__link',
            closeButton: '.close',
            langNavBtn: '.cmp-languagenavigation__trigger',
            langNavActive: '.cmp-languagenavigation__item--active',
            portalNavBtn: '.header__portal-trigger',
            langNavCmp: '.cmp-languagenavigation',
            portalNavCmp: '.header__portal',
            langNavContent: '.cmp-languagenavigation__group',
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
            this.showLangNav();
            this.showPortalNav();
        }.bind(this);

        this.showLangNav = function () {
            const $langNavBtn = this.$el.find(this.selectors.langNavBtn);
            const $langNavCmp = this.$el.find(this.selectors.langNavCmp);
            const $activeLang = $langNavCmp.find(this.selectors.langNavActive);

            // set current lang. trigger text
            // LANG placeholder in author-mode, as active lang is not displayed in author by AEM
            if ($langNavBtn.hasClass('author-mode')) {
                $langNavBtn.text('LANG');
            } else {
                $langNavBtn.text($activeLang.text());
            }


            $langNavBtn.removeClass('visibility-hidden');

            $langNavBtn.on('click', () => {
                this.closePortalNav();
                $langNavCmp.toggleClass('is-open');
            });
        }.bind(this);

        this.closeLangNav = function () {
            const $langNavCmp = this.$el.find(this.selectors.langNavCmp);
            $langNavCmp.removeClass('is-open');
        }.bind(this);

        this.showPortalNav = function () {
            const $portalNavBtn = this.$el.find(this.selectors.portalNavBtn);
            const $portalNavCmp = this.$el.find(this.selectors.portalNavCmp);

            $portalNavBtn.on('click', () => {
                this.closeLangNav();
                $portalNavCmp.toggleClass('is-open');
            });
        }.bind(this);

        this.closePortalNav = function () {
            const $portalNavCmp = this.$el.find(this.selectors.portalNavCmp);
            $portalNavCmp.removeClass('is-open');
        }.bind(this);

        this.showAndHideDropdown = function () {
            var $links = this.$el.find(this.selectors.navigationLink);

            $links.on('click', (event) => {
                var $target = $(event.target);
                var $link = undefined;
                $('body').addClass('scroll-hidden');
                $('.header__dropdown-wrapper').addClass('dropdown-wrapper-open-fx');

                if ($target.is("li")) {
                    $link = $target;
                }
                else {
                    $link = $target.closest('li');
                }

                var navItemAttr = $link && $link.attr(this.atributes.dropdownItem);

                if (navItemAttr.trim().length > 0) {
                    var $dropdownItem = this.$el.find("div[name='" + navItemAttr + "']");

                    if ($dropdownItem && $dropdownItem.length > 0) {
                        this.hideAllDropDowns();
                        this.removeActive();
                        this.closePortalNav();
                        this.closeLangNav();
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
                if($(window).width() > 1024){
                    $('body').removeClass('scroll-hidden');
                }
                $('.header__dropdown-wrapper').removeClass('dropdown-wrapper-open-fx');
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
    let isVisible = false;
    function initSearchbar() {
        const $searchBarWrapper = $(".headerSearchBar");

        $("#search-trigger").on("click", function () {
            const $iconClosed = $("#trigger-icon-closed");
            const $iconOpened = $("#trigger-icon-opened");
            const $overlay = $(".search-trigger__overlay");

            isVisible = !isVisible;

            if (isVisible) {
                $searchBarWrapper.toggle(true);
                $iconOpened.toggle(true);
                $iconClosed.toggle(false);
                $overlay.toggle(true);
            } else {
                $searchBarWrapper.toggle(false);
                $iconOpened.toggle(false);
                $iconClosed.toggle(true);
                $overlay.toggle(false);
            }
        });

        if (window.innerWidth < 1025) {
            $searchBarWrapper.addClass("mobile");
        } else {
            $searchBarWrapper.removeClass("mobile");
        }

        window.addEventListener("resize", () => {
            setTimeout(() => {
                if (window.innerWidth > 1024) {
                    $searchBarWrapper.removeClass("mobile");
                    $searchBarWrapper.toggle(true);
                } else {
                    $searchBarWrapper.addClass("mobile");

                    if (isVisible) {
                        $searchBarWrapper.toggle(true);
                    } else {
                        $searchBarWrapper.toggle(false);
                    }
                }
            }, 500);
        });
    }

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
    function injectLangNav($headerNav) {
        const $langNav = $headerNav.find('.cmp-languagenavigation');
        const $langNavMobilePlaceholder = $headerNav.find('.header__lang-nav-mobile');
        $langNavMobilePlaceholder.append($langNav);
    }

    function burgermenuinit($headerNav) {
        var clickDelay = 100,
            clickDelayTimer = null;
        $('.burger-click-region').on('click', function () {
            if (clickDelayTimer === null) {
                var $burger = $(this);
                $('body').addClass('scroll-hidden');
                $burger.toggleClass('active');
                $burger.parent().toggleClass('is-open');
                $('nav.navigation').slideDown();

                if (!$burger.hasClass('active')) {
                    $burger.addClass('closing');
                    $('body').removeClass('scroll-hidden');
                    $('nav.navigation').slideUp();
                }
                clickDelayTimer = setTimeout(function () {
                    $burger.removeClass('closing');
                    injectLangNav($headerNav);
                    clearTimeout(clickDelayTimer);
                    clickDelayTimer = null;
                }, clickDelay);
            }
        });
    }
    $(document).ready(function () {
        const $headerNav = $("header[data-component-name='navigation']").not('.header-hide-nav');
        if ($headerNav.length) {
            burgermenuinit($headerNav);
            initSearchbar();

            if ($headerNav[0].dataset.initialized !== "true") {
                new NotificationModule($headerNav[0]);
            }
        }
    });
}());