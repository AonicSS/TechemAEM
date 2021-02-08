(function($) {
    "use strict";

    function NewsArticleModule(el) {
            el.dataset.initialized = true;

            this.$el = $(el);

            this.selectors = {
                  newsLink: '.cmp-newsArticles_link',
                  newsIcon: '.cmp-newsArticles-icon',
                  textStage: '.cmp-newsArticles_text-stage'
            };

            this.init = function() {
                var $newsLinks = this.$el.find(this.selectors.newsLink);

                this.mouseOver($newsLinks);
                this.mouseOut($newsLinks);
            }.bind(this);

            this.mouseOver = function($newsLinks) {
                   $newsLinks.on('mouseover', (event) => {
                     var $target = $(event.target);
                     var $newsLink = $target && $target.closest(this.selectors.textStage);
                     var articleWidth = $newsLink && $newsLink.width() / 2 - 30;
                     var $icon = $newsLink.find(this.selectors.newsIcon);

                     if($icon.length > 0) {
                        $icon.css('transform', 'translateX(' + articleWidth + 'px)');
                     }
                });
            }.bind(this);

            this.mouseOut = function($newsLinks) {
                $newsLinks.on('mouseout', (event) => {
                     var $target = $(event.target);
                     var $newsLink = $target && $target.closest(this.selectors.textStage);
                     var articleWidth = $newsLink && $newsLink.width();
                     var $icon = $newsLink.find(this.selectors.newsIcon);

                     if($icon.length > 0) {
                        $icon.css('transform', 'translateX(' + 0 + 'px)');
                     }
                });
            }.bind(this);


            this.init();

            return this;
    }

    $(document).ready(function() {
        var $newsArticle = $("div[data-component-name='news-article']");
        Array.prototype.forEach.call($newsArticle, function(element) {
            if (element && element.dataset.initialized !== "true") {
                new NewsArticleModule(element);
            }
        });
    });
}(jQuery));

function resizeGridItem(item){
    grid = document.getElementsByClassName("cmp-newsArticles__container")[0];
    rowHeight = parseInt(window.getComputedStyle(grid).getPropertyValue('grid-auto-rows'));
    rowGap = parseInt(window.getComputedStyle(grid).getPropertyValue('grid-row-gap'));
    rowSpan = Math.ceil((item.querySelector('.cmp-newsArticles_link').getBoundingClientRect().height+rowGap)/(rowHeight+rowGap));
      item.style.gridRowEnd = "span "+rowSpan;
  }
  
  function resizeAllGridItems(){
    allItems = document.getElementsByClassName("cmp-newsArticles_text-stage");
    Array.from(allItems).forEach((item) => {
      resizeGridItem(item);
    });
  }
  
  function resizeInstance(instance){
      item = instance.elements[0];
    resizeGridItem(item);
  }
  
  window.onload = resizeAllGridItems();
  window.addEventListener("resize", resizeAllGridItems);
  
  allItems = document.getElementsByClassName("cmp-newsArticles_text-stage");
  Array.from(allItems).forEach((item) => {
    imagesLoaded( item, resizeInstance);
  });
