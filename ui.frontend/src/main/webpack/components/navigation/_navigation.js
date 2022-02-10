(function () {
    "use strict";

    function NotificationModule(el) {
        el.dataset.initialized = true;

        this.$el = $(el);

        this.selectors = {
            headerDropdown: '.header__dropdown-content',
            navigationLink: '.navigation__link',
            closeButton: '.close',
            langNav: '.cmp-languagenavigation',
            langNavBtn: '.cmp-languagenavigation__trigger',
            langNavActive: '.cmp-languagenavigation__item--active',
            portalNavBtn: '.header__portal-trigger',
            targetNavBtn: '.header__target-trigger',
            langNavCmp: '.cmp-languagenavigation',
            portalNavCmp: '.header__portal',
            targetNavCmp: '.header__target',
            langNavContent: '.cmp-languagenavigation__group',
            headerButton: ".header__button",
            active: "navigation-active",
            hidden: 'header__dropdown-hidden',
            navHidden: 'header-hide-nav',
            burgerClickRegion: '.burger-click-region'
        };

        this.atributes = {
            dropdownItem: "dropdown-item",
            link: "button-link",
            content: "content",
            protocol: "https://"
        };

        this.init = function () {
            this.burgermenuinit();
            this.showAndHideDropdown();
            this.closeDropDown();
            this.redirectButton();
            this.showLangNav();
            this.showPortalNav();
            this.showTargetNav();
            this.hideNavigationOnSizeChange();
        }.bind(this);

        this.showLangNav = function () {
            const $langNavBtn = this.$el.find(this.selectors.langNavBtn);
            const $langNavCmp = this.$el.find(this.selectors.langNavCmp);
            const $activeLang = $langNavCmp.find(this.selectors.langNavActive);

            /* Don't show langnav if navigation div is empty -> there are <= 1 language navigation items present */
            if(!$.trim($langNavCmp.html())) {
                return;
            }

            // set current lang. trigger text
            // LANG placeholder in author-mode, as active lang is not displayed in author by AEM
            if ($langNavBtn.hasClass('author-mode')) {
                $langNavBtn.text('LANG');
            } else {
                $langNavBtn.text($activeLang.text());
            }

            $langNavCmp.parent().removeClass('hidden'); // Show Langnav
            $langNavBtn.removeClass('visibility-hidden');

            $langNavBtn.on('click', () => {
                this.closePortalNav();
                this.closeTargetNav();
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

        this.showTargetNav = function () {
            const $targetNavBtn = this.$el.find(this.selectors.targetNavBtn);
            const $targetNavCmp = this.$el.find(this.selectors.targetNavCmp);

            $targetNavBtn.on('click', () => {
                this.closeLangNav();
                $targetNavCmp.toggleClass('is-open');
            });
        }.bind(this);

        this.closeTargetNav = function () {
            const $targetNavCmp = this.$el.find(this.selectors.targetNavCmp);
            $targetNavCmp.removeClass('is-open');
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

        this.injectLangNav = function ($destinationLangDiv) {
            const $langNav = this.$el.find(this.selectors.langNav);
            const $destinationDiv = this.$el.find($destinationLangDiv);
            /* Inject langnav ONLY if the langnav element has content (lang nav items are > 1) */
            if($.trim($langNav.html())) {
                $destinationDiv.append($langNav);
            }
        }.bind(this);

        this.burgermenuinit = function () {
            const isNavHidden = this.$el.hasClass(this.selectors.navHidden);
            const portalCmp = this.$el.find(this.selectors.portalNavCmp);
            var langNav = this.$el.find(this.selectors.langNav);
            var $burger = this.$el.find(this.selectors.burgerClickRegion);

            if(isNavHidden && !$.trim(portalCmp.html()) && !$.trim(langNav.html())) {
                $burger.hide();
                return;
            }

            var clickDelay = 100,
                clickDelayTimer = null;
            $($burger).on('click', () => {
                if (clickDelayTimer === null) {
                    $('body').addClass('scroll-hidden');
                    $burger.toggleClass('active');
                    $burger.parent().toggleClass('is-open');
                    $('nav.navigation').slideDown();
    
                    if (!$burger.hasClass('active')) {
                        $burger.addClass('closing');
                        $('body').removeClass('scroll-hidden');
                        $('nav.navigation').slideUp();
                    }else {
                        this.injectLangNav('.header__lang-nav-mobile');
                    }

                    clickDelayTimer = setTimeout(() => {
                        $burger.removeClass('closing');
                        clearTimeout(clickDelayTimer);
                        clickDelayTimer = null;
                    }, clickDelay);
                }
            });
        }.bind(this);

        this.hideNavigationOnSizeChange = function () {
            const isNavHidden = this.$el.hasClass(this.selectors.navHidden);
            const burgerClickRegion = this.$el.find(this.selectors.burgerClickRegion);
            var langNavContainerDesktop = this.$el.find('.header__lang-nav');
            var langNavContainerMobile = this.$el.find('.header__lang-nav-mobile');
            
            /* Resize event listener to properly inject lang nav and hide burger menu when resizing window from phone view to desktop */
            window.addEventListener('resize', () => {
                var isBurgerActive = burgerClickRegion.hasClass('active');

                if(window.innerWidth > 1024) {
                    if(isNavHidden && isBurgerActive) {
                        burgerClickRegion.click();
                    }
                    if(!$.trim(langNavContainerDesktop.html())) {
                        this.injectLangNav('.header__lang-nav');
                    }
                }
                if(window.innerWidth < 1024 && isBurgerActive && !$.trim(langNavContainerMobile.html())) {
                    this.injectLangNav('.header__lang-nav-mobile');
                }
            }, true);
        }.bind(this);

        this.init();

        return this;
    }

    /* ---------------------------------------------
                        search
    --------------------------------------------- */
    let isVisible = false;
    function initSearchbar($searchBarWrapper) {

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

    $(document).ready(function () {
        const $searchBarWrapper = $(".headerSearchBar");
        if($searchBarWrapper.length) {
            initSearchbar($searchBarWrapper);
        }

        const $headerNav = $("header[data-component-name='navigation']");
        if ($headerNav.length) {
            if ($headerNav[0].dataset.initialized !== "true") {
                new NotificationModule($headerNav[0]);
            }
        }
    });
}());