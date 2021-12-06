/* global ANSWERS */
import Typed from 'typed.js';

let yextConf = {};

function addYextComponent(scope) {
    const placeholders = document.querySelectorAll('.search-bar');

    placeholders.forEach((placeholder) => {
        scope.addComponent("SearchBar", {
            container: "#" + placeholder.id,
            name: placeholder.id,
            redirectUrl: placeholder.dataset.resultsPage,
            customIconUrl: placeholder.dataset.searchIcon,
            clearButton: true
        });
    });
}

function initTypedJs() {
    const searchInputs = document.querySelectorAll('.search-bar .yxt-SearchBar-input');
    let url = 'https://liveapi-cached.yext.com/v2/accounts/me/answers/autocomplete';
    url += '?v=20190101';
    url += '&api_key=' + yextConf.apiKey;
    url += '&sessionTrackingEnabled=false';
    url += '&experienceKey=' + yextConf.experienceKey;
    url += '&input=';
    url += '&version=' + yextConf.experienceVersion;
    url += '&locale=' + yextConf.locale;

    fetch(url)
        .then(response => response.json())
        .then(data => {
            const strings = data.response.results.map(function(r) {
                return r.value;
            });

            const options = {
                strings: strings,
                showCursor: true,
                cursorChar: "|",
                typeSpeed: 45,
                backSpeed: 20,
                smartBackspace: true,
                loop: true,
                startDelay: 500,
                backDelay: 2000,
                attr: "placeholder",
            };

            searchInputs.forEach((searchInput) => {
                const typed = new Typed(searchInput, options);
            });
        });
}

function initAnswers() {
    const yextScript = document.querySelector('#answers-script');

    yextConf = {
        apiKey: yextScript.dataset.apikey,
        experienceKey: yextScript.dataset.experiencekey,
        businessId: yextScript.dataset.businessid,
        experienceVersion: yextScript.dataset.experienceversion,
        locale: yextScript.dataset.locale,
        redirect: yextScript.dataset.redirect
    };

    ANSWERS.init({
        apiKey: yextConf.apiKey,
        experienceKey: yextConf.experienceKey,
        businessId: yextConf.businessId,
        experienceVersion: yextConf.experienceVersion,
        templateBundle: TemplateBundle.default,
        locale: yextConf.locale,
        onReady: function () {
            addYextComponent(this);
            initTypedJs();
        },
    });
}

function initYext() {
    const searchCmp = document.querySelectorAll("[data-component-name='search-bar']");
    try {
        if(ANSWERS && !ANSWERS.components._activeComponents.length && searchCmp.length) {
            ANSWERS.domReady(initAnswers);
        }
    } catch(e) {}
}

window.addEventListener('DOMContentLoaded', (event) => {
    const yextScript = document.querySelector('#answers-script');
    const searchCmp = document.querySelectorAll("[data-component-name='search-bar']");
    if (yextScript && searchCmp.length) {
        yextScript.addEventListener('load', function() {
            initYext(); 
        });
        initYext(); 
    }
});