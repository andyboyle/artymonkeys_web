jQuery(document).ready(function () {
    jQuery(".menu_trigger").click(function () {
        jQuery("#main_menu").slideToggle(400, function () {
            jQuery(this).toggleClass("nav-expanded");
            //.css('display','');
        });
    });
});