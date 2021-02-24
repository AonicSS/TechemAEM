import imagesLoaded from 'imagesloaded';
(function($) {
    "use strict";

    function resizeGridItem(item){
      const grid = document.getElementsByClassName("cmp-newsArticles__container")[0];
      const rowHeight = parseInt(window.getComputedStyle(grid).getPropertyValue('grid-auto-rows'));
      const rowGap = parseInt(window.getComputedStyle(grid).getPropertyValue('grid-row-gap'));
      const rowSpan = Math.ceil((item.querySelector('.cmp-newsArticles_link').getBoundingClientRect().height+rowGap)/(rowHeight+rowGap));
        item.style.gridRowEnd = "span "+rowSpan;
    }
    
    function resizeAllGridItems(){
      const allItems = document.getElementsByClassName("cmp-newsArticles_text-stage");
      Array.from(allItems).forEach((item) => {
        resizeGridItem(item);
      });
    }
    
    function resizeInstance(instance){
        const item = instance.elements[0];
      resizeGridItem(item);
    }
    
    window.onload = resizeAllGridItems();
    window.addEventListener("resize", resizeAllGridItems);
    
    const allItems = document.getElementsByClassName("cmp-newsArticles_text-stage");
    Array.from(allItems).forEach((item) => {
      imagesLoaded(item, resizeInstance);
    });
    
}(jQuery));

