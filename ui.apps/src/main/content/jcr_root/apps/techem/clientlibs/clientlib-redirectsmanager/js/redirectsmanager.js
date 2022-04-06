(function () {
    "use strict";

    function RedirectsManager(el) {

        this.el = $(el);

        this.el.data("initialized", "true");

        this.actions = [
            {
                name: "techem.redirects.delete",
                handler: (name, el, config, collection, selections) => this.doDelete(collection, selections)
            },
            {
                name: "techem.redirects.edit",
                handler: (name, el, config, collection, selections) => this.doEdit(selections)
            },
            {
                name: "techem.redirects.create",
                handler: (name, el, config, collection, selections) => this.doCreate()
            },
            {
                name: "techem.redirects.activate",
                handler: (name, el, config, collection, selections) => this.doReplication(name, selections, collection)
            },
            {
                name: "techem.redirects.deactivate",
                handler: (name, el, config, collection, selections) => this.doReplication(name, selections, collection)
            },
            {
                name: "techem.redirects.export",
                handler: (name, el, config, collection, selections) => this.doExport(el, collection, selections)
            },
            {
                name: "techem.redirects.check",
                handler: (name, el, config, collection, selections) => this.doCheck(collection, selections)
            },
            {
                name: "techem.redirects.preview",
                handler: (name, el, config, collection, selections) => this.doPreview(selections)
            }
        ];

        this.selectors = {
            collectionID: ".custom-redirects-list",
            editForm: "#editRuleForm",
            editFormSubmitBtn: "#editBtnSubmit",
            editFormCloseBtn: "#editDiagCloseBtn",
            createDialog: "#editRuleDiag",
            filterDialog: "#filterDiag",
            filterDialogForm: "#filterForm",
            filterDialogSubmitBtn: "#filterBtnSubmit",
            filterDialogCloseBtn: "#filterDiagCloseBtn",
            filterMktField: ".filter-market-field",
            importDialog: "#importRulesDiag",
            importForm: "#importRulesForm",
            importFormData: "#importRulesData",
            importDiagSubmitBtn: "#importBtnSubmit",
            importDiagCloseBtn: "#importRulesDiagCloseBtn",
            importDiag: "#importRulesDiag",
            exportAllBtn: "#exportBtnAll",
            editFormFromField: ".edit-rule-from",
            editFormToField: ".edit-rule-to",
            editFormMktField: ".edit-rule-market",
            editFormKeepQSField: ".edit-rule-queryString",
            editFormCodeField: ".edit-rule-code",
            editFormUntilField: ".edit-rule-date",
            graniteTitle: ".granite-title"
        };

        this.constants = {            
            filterByQueryStr: "filterBy",
            redirManagerServletURL: "/eu/techem/redirmngr",
            redirManagerContentURL: "/content/techem/content",
            foundationRegistry: "foundation-registry",
            foundationCollection: "foundation-collection",
            foundationUI: "foundation-ui",
            foundationCollectionAction: "foundation.collection.action.action"
        };

        this.actionsConstants = {
            create: "create",
            delete: "delete",
            import: "import",
            edit: "edit",
            activate: "activate",
            deactivate: "deactivate",
            export: "export",
            check: "check",
            filter: "filter"
        };

        this.forbiddenPaths = [
            "/apps/?|/apps/.+?",
            "/bin/?|/bin/.+?",
            "/eu/?|/eu/.+?",
            "/etc/?|/etc/.+?",
            "/home/?|/home/.+?",
            "/libs/?|/libs/.+?",
            "/saml_login/?|/saml_login/.+?",
            "/system/?|/system/.+?",
            "/tmp/?|/tmp/.+?",
            "/var/?|/var/.+?",
            "/mnt/?|/mnt/.+?"
        ];

        this.init = function() {
            this.initFoundationRegistry();
            this.initCreateDialog();
            this.initImport();
            this.initFilterDiag();
        }.bind(this);

        this.foundationUI = $(window).adaptTo(this.constants.foundationUI);

        this.initFoundationRegistry = function() {
            const foundationRegistry = $(window).adaptTo(this.constants.foundationRegistry);

            if(foundationRegistry != null) {
                this.actions.forEach(
                    action => foundationRegistry.register(this.constants.foundationCollectionAction, action)
                );
            }
        }.bind(this);

        this.initCreateDialog = function() {
            const dialogEl = $(this.selectors.createDialog);
            const sbmtBtn = $(dialogEl).find(this.selectors.editFormSubmitBtn);
            const closeBtn = $(dialogEl).find(this.selectors.editFormCloseBtn);
            var form = $(dialogEl).find(this.selectors.editForm);
            var codeField = $(form).find(this.selectors.editFormCodeField);
            var toField = $(form).find(this.selectors.editFormToField);
            var fromField = $(form).find(this.selectors.editFormFromField);
            var keepQSField = $(form).find(this.selectors.editFormKeepQSField);

            $(codeField).on("change", function() {
                if(this.value == 410) {
                    toField.prop("disabled", true);
                    keepQSField.prop("disabled", true);
                }else {
                    toField.prop("disabled", false);
                    keepQSField.prop("disabled", false);
                }
            });

            $(form).on("submit", (e) => {
                e.preventDefault();

                var fromVal = $(fromField).val();

                if(fromVal != null && fromVal.length > 0) {
                    var pathsForbidden = this.forbiddenPaths.filter((item) => {
                        return new RegExp("^" + item + "$").test(fromVal);
                    });

                    if(pathsForbidden.length) {
                        this.foundationUI.notify("Info", "The source path '" + fromVal + "' is not allowed.", "error");
                        return false;
                    }
                }

                var invalidFields = $(form).find("input[aria-required]").filter(function() {
                    return !$(this).prop("disabled") && !this.value;
                }).addClass("is-invalid").attr("aria-invalid", "true");

                $(form).find("input[aria-required]").filter(function() {
                    return this.value;
                }).removeClass("is-invalid").attr("aria-invalid", "false");

                if(invalidFields.length > 0) {
                    $(sbmtBtn).attr("disabled", true);
                    return false;
                }

                var isEdit = $(dialogEl).data("edit-mode");
                var actionType = (isEdit ? this.actionsConstants.edit : this.actionsConstants.create);
                var formData = $(form).serializeArray();

                if(isEdit) {
                    var itemPath = $(dialogEl).data("edit-mode-path");
                    formData.push({ name: "path", value: itemPath });
                }
                
                $(sbmtBtn).attr("disabled", true);
                $.post(this.constants.redirManagerServletURL + "?action=" + actionType, $.param(formData))
                .done(() => {
                    $(closeBtn).trigger("click");
                    $(this.selectors.collectionID).adaptTo(this.constants.foundationCollection).reload();
                    this.updateMarkets();
                    this.foundationUI.notify("Info", "Successfully " + (actionType == "edit" ? "edited" : "created" ) + " rule.", "success");
                }).fail(() => {
                    this.foundationUI.notify("Info", "Could not " + actionType + " rule.", "error");
                });
            });
        }.bind(this);

        this.initFilterDiag = function() {
            const dialogEl = $(this.selectors.filterDialog);
            var form = $(dialogEl).find(this.selectors.filterDialogForm);
            var mktField = $(form).find(this.selectors.filterMktField);
            var searchParams = new URLSearchParams(window.location.search);

            if(searchParams.has(this.constants.filterByQueryStr) && searchParams.get(this.constants.filterByQueryStr).length) {
                var qVal = searchParams.get(this.constants.filterByQueryStr);
                var option = $(mktField).find("[value=" + qVal + "]")[0];
                $(option).attr("selected", "");
                $(this.selectors.graniteTitle).append(": " + qVal);
            }

            $(form).on("submit", (e) => {
                e.preventDefault();
                var mktVal = $(mktField).val();
                
                window.location.href = mktVal.length > 0 ? "?filterBy=" + mktVal : window.location.href.split('?')[0];
            });
        }.bind(this);

        this.initImport = function() {
            const dialogEl = $(this.selectors.importDialog);
            const importSubmitBtn = $(dialogEl).find(this.selectors.importDiagSubmitBtn);
            const importCloseBtn = $(dialogEl).find(this.selectors.importDiagCloseBtn);
            var importForm = $(dialogEl).find(this.selectors.importForm);
            var formData = $(importForm).find(this.selectors.importFormData);

            $(importForm).on("submit", (e) => {
                e.preventDefault();

                if($(formData).val().length > 0) {
                    var d = new FormData(importForm[0]);
                    $(importSubmitBtn).prop("disabled", true);
                    this.foundationUI.notify("Info", "Please wait, your file is being processed. This may take a while.", "info");

                    $.ajax({
                        type: "POST",
                        enctype: "multipart/form-data",
                        url: this.constants.redirManagerServletURL + "?action=" + this.actionsConstants.import,
                        data: d,
                        processData: false,
                        contentType: false,
                        cache: false
                    }).done((data) => {
                        var hasErrors = $(data).length;
                        if(hasErrors) {
                            this.foundationUI.alert("Warning", "There were import errors. Rules that were not valid were skipped. Please check the following rows: " + data + ".", "warning");
                        }

                        $(importForm).trigger("reset");
                        $(importCloseBtn).trigger("click");
                        $(this.selectors.collectionID).adaptTo(this.constants.foundationCollection).reload();
                        this.updateMarkets();
                        this.foundationUI.notify("Info", hasErrors ? "Exceptions occured while importing." : "Successfully imported rules.", hasErrors ? "warning" : "success");
                    }).fail(() => {
                        this.foundationUI.notify("Info", "Could not import rules.", "error");
                    });
                }
            });
        }.bind(this);

        this.doExport = function(el, collection, selections) {
            var exportAll = $(el).is(this.selectors.exportAllBtn);
            var maxExportItems = $(el).data("max-selection");
            var selectedItems = selections.map(function(item) { return $(item).data("foundation-collection-item-id"); });
            var listItems = "";
            var hasElements = $(collection).find(".foundation-collection-item").length > 0;
            
            if(exportAll && !hasElements) {
                return;
            }

            if(!exportAll && selections.length > maxExportItems) {
                this.foundationUI.notify("Info", "Maximum export size limit exceeded. Please select less than " + maxExportItems + " items.", "error");
                return;
            }

            selectedItems.forEach(el => listItems += (listItems == "" ? el : "," + el));

            window.location = this.constants.redirManagerServletURL + "?action=" + this.actionsConstants.export + "&paths=" + (exportAll ? "all" : encodeURIComponent(listItems)); // Fix url too long here
            this.foundationUI.notify("Info", "Starting download...", "success");
        }.bind(this);

        this.doReplication = function(name, selections, collection) {
            var replicationType = name.split(".")[2];
            var replicationTitle = replicationType.charAt(0).toUpperCase() + replicationType.slice(1);
            var selectedItems = selections.map(function(item) { return $(item).data("foundation-collection-item-id"); });

            var diag = this.createDiag(
                "replicate_diag_" + replicationType, 
                replicationTitle + " Rules",
                "Are you sure you want to " + replicationType + " " + selectedItems.length + " rule" + (selectedItems.length > 1 ? "s" : "") + "?",
                replicationTitle
            );
    
            var listItems = "";
            selectedItems.forEach(el => listItems += (listItems == "" ? el : "," + el));
    
            diag.on("click", "#acceptButton", () => {
                this.foundationUI.notify("Info", "Started replication. This may take a while.", "info");
                $.post(this.constants.redirManagerServletURL, { paths: listItems, action: this.actionsConstants[replicationType] })
                    .done(() => {
                        $(collection).adaptTo(this.constants.foundationCollection).reload();
                        this.foundationUI.notify("Info", "Successfully " + replicationType + "d " + selectedItems.length + " rule" + (selectedItems.length > 1 ? "s" : "") + ".", "success");
                    }).fail(() => {
                        this.foundationUI.notify("Info", "Could not " + replicationType + " rule.", "error");
                    });
                diag.hide();
            });

            diag.show();
        }.bind(this);

        this.doCheck = function(collection, selections) {
            var selectedItems = selections.map(function(item) { return $(item).data("foundation-collection-item-id"); });

            var listItems = "";
            selectedItems.forEach(el => listItems += (listItems == "" ? el : "," + el));
            this.foundationUI.notify("Info", "Check has begun. This may take a while.", "info");

            $.post(this.constants.redirManagerServletURL, { paths: listItems, action: this.actionsConstants.check })
            .done(() => {
                this.foundationUI.notify("Info", "Successfuly checked rules.", "success");
                $(collection).adaptTo(this.constants.foundationCollection).reload();
            }).fail(() => {
                this.foundationUI.notify("Info", "Could not check rules.", "error");
            });
        }.bind(this);

        this.doCreate = function() {
            const dialogEl = $(this.selectors.createDialog);
            var isEdit = $(dialogEl).data("edit-mode");
            var form = $(dialogEl).find(this.selectors.editForm);
            var toField = $(form).find(this.selectors.editFormToField);
            var keepQSField = $(form).find(this.selectors.editFormKeepQSField);
            $(form).trigger("reset");

            toField.prop("disabled", false);
            keepQSField.prop("disabled", false);

            if(isEdit) { 
                $(dialogEl).removeData("edit-mode"); 
                $(dialogEl).removeData("edit-mode-path");
            }
        }.bind(this);

        this.doEdit = function(selection) {
            const dialogEl = $(this.selectors.createDialog);
            var form = $(dialogEl).find(this.selectors.editForm);
            /* TO-DO: Edit rule from search result functionality 
            var urlParams = new URLSearchParams(window.location.search);
            var isEdit = urlParams.has("rule") && urlParams.get("rule") != null;
            selection = (isEdit ? $('[data-foundation-collection-item-id="' + urlParams.get("rule") + '"]') : selection);
            */
            var ruleMkt = $(selection).data("rule-market");
            var ruleKeepQS = $(selection).data("rule-keep-qs") !== undefined;
            var selectedItemData = $(selection).find("td");

            if(selectedItemData.length > 0) {
                var fromField = $(form).find(this.selectors.editFormFromField);
                var toField = $(form).find(this.selectors.editFormToField);
                var codeField = $(form).find(this.selectors.editFormCodeField);
                var untilField = $(form).find(this.selectors.editFormUntilField);
                var marketField = $(form).find(this.selectors.editFormMktField);
                var keepQSField = $(form).find(this.selectors.editFormKeepQSField);
                var selectedCodeOption = $(codeField).find("[value=" + $(selectedItemData[3]).text() + "]")[0];

                $(form).trigger("reset");
                $(dialogEl).data("edit-mode", true);
                $(dialogEl).data("edit-mode-path", $(selection).data("foundation-collection-item-id"));
                $(fromField).val($(selectedItemData[1]).text().replace(/\s/g,''));
                $(toField).val($(selectedItemData[2]).text().replace(/\s/g,''));
                $(marketField).val(ruleMkt != undefined ? ruleMkt : "");
                $(selectedCodeOption).attr("selected", "");
                $(untilField).val($(selectedItemData[4]).data("dateTimeValue"));
                if(ruleKeepQS) { $(keepQSField).attr("checked", ""); }
                if($(selectedItemData[3]).text() == "410") {
                    toField.prop("disabled", true);
                    keepQSField.prop("disabled", true);
                }else {
                    toField.prop("disabled", false);
                    keepQSField.prop("disabled", false);
                }
            }
        }.bind(this);

        this.doDelete = function(collection, selections) {
            var selectedItems = selections.map(function(item) { return $(item).data("foundation-collection-item-id"); });
            var publishedItems = $(selections).find(".published-col").filter(function() {
                return $(this).text().trim().toLowerCase() != 'not published';
            });
            var msg = publishedItems.length > 0 ? 
            "Out of the " + selectedItems.length + " selected rule" + (selectedItems.length > 1 ? "s" : "") + ", there " + (publishedItems.length > 1 ? "are " : "is ") + publishedItems.length + " <b>PUBLISHED</b> rule" + (publishedItems.length > 1 ? "s" : "") + "! It is recommended to unpublish any published rule before deleting. <br/> Do you want to continue anyway?" : 
            "Are you sure you want to remove " + selectedItems.length + " rule" + (selectedItems.length > 1 ? "s" : "") + "? <b>THIS CANNOT BE UNDONE</b>!";
            
            var diag = this.createDiag(
                "delete_diag",
                "Delete Rules",
                msg,
                "Delete"
            );
    
            var listItems = "";
            selectedItems.forEach(el => listItems += (listItems == "" ? el : "," + el));
    
            diag.on("click", "#acceptButton", () => {
                $.post(this.constants.redirManagerServletURL, { paths: listItems, action: this.actionsConstants.delete })
                    .done(() => {
                        $(collection).adaptTo(this.constants.foundationCollection).reload();
                        this.updateMarkets();
                        this.foundationUI.notify("Info", "Successfully deleted " + selectedItems.length + " rule" + (selectedItems.length > 1 ? "s" : "") + ".", "success");
                    }).fail(() => {
                        this.foundationUI.notify("Info", "Could not delete rule.", "error");
                    });
                diag.hide();
            });

            diag.show();
        }.bind(this);

        this.doPreview = function(selections) {
            var ruleData = "";
            /* Check if the node is a coral masonry item so that this works from the search screen. */
            if(selections[0].nodeName == "CORAL-MASONRY-ITEM") {
                ruleData = $(selections[0]).find(".rule-from-omnisearch coral-card-property-content")[0];
            }else {
                ruleData = $(selections[0]).find("td")[1];
            }

            var ruleFrom = $(ruleData).text();

            if(ruleFrom.length) {
                var ruleURL = ruleFrom.startsWith("http") ? ruleFrom : this.constants.redirManagerContentURL + ruleFrom.replaceAll(/(\?.*|#.*)/ig, "") + "?wcmmode=disabled";
                window.open(ruleURL, "_blank");
            }
        }.bind(this);

        this.createDiag = function(diagId, diagHeader, diagContent, okayBtnMsg) {
            var onlyClose = okayBtnMsg == null;
            var dialog = new Coral.Dialog().set({
                id: diagId,
                header: {
                    innerHTML: diagHeader
                },
                content: {
                    innerHTML: diagContent
                },
                footer: {
                    innerHTML: '<button id="cancelButton" is="coral-button" variant="default" coral-close>' + (onlyClose ? "Close" : "Cancel") + '</button>' + (onlyClose ? "" : "<button id=\"acceptButton\" is=\"coral-button\" variant=\"primary\">" + okayBtnMsg + "</button>")
                }
            });
            return dialog;
        }.bind(this);

        this.generateToast = function(msg, time, variant) {
            var toast = new Coral.Toast().set({
                content: {
                  textContent: msg
                },
                autoDismiss: time,
                variant: variant
            });
            return toast;
        }.bind(this);

        this.updateMarkets = function() {
            var mrktDropdown = $(this.selectors.filterMktField);
            $(mrktDropdown).find("coral-select-item").filter(function() {
                return this.value;
            }).remove();

            $.ajax({ 
                type: "GET",
                url: this.constants.redirManagerServletURL + "?markets",
                dataType: "json",
                success: function (data) {
                    $.each(data, function(index, element) {
                        var item = "<coral-select-item value=\"" + element + "\">" + element + "</coral-select-item>";
                        $(mrktDropdown).append(item);
                    });
                }
            });
        }.bind(this);

        this.init();

        return this;
    }

    $(document).ready(function () {
        const redirectsTable = $(".custom-redirects-list").length > 0 ? $(".custom-redirects-list") : $("#granite-omnisearch-result-content");

        if (redirectsTable.length && redirectsTable[0].dataset.initialized !== "true") {
            new RedirectsManager(redirectsTable);
        }
    });
}());