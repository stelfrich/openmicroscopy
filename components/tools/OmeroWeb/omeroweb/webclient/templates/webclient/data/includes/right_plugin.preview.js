{% comment %}
<!--
  Copyright (C) 2012 University of Dundee & Open Microscopy Environment.
  All rights reserved.

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU Affero General Public License as
  published by the Free Software Foundation, either version 3 of the
  License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Affero General Public License for more details.

  You should have received a copy of the GNU Affero General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
-->
{% endcomment %}

<script>

$(document).ready(function() {
    
    // this script is an 'include' within a django for-loop, so we can get our index:
    var preview_tab_index = {{ forloop.counter }};

    var update_preview_tab = function() {
        
        // get the selected id etc
        var selected = $("body").data("selected_objects.ome");
        if (!selected) return;
        var orel = selected[0]["id"].split("-")[0];
        if ((orel!="image") && (orel != "well")) return;

        if (selected.length > 1) return;
        var oid = selected[0]["id"];

        // if the tab is visible and not loaded yet...
        $metadata_preview = $("#preview_tab");
        if ($metadata_preview.is(":visible") && $metadata_preview.is(":empty")) {
            var well_index = selected[0]["index"];
            var url = null;
            if (selected[0]["share"]) {
                url = '{% url webindex %}metadata_preview/'+orel+'/'+oid.split("-")[1]+'/'+selected[0]["share"]+'/?index='+well_index;
            } else {
                url = '{% url webindex %}metadata_preview/'+orel+'/'+oid.split("-")[1]+'/?index='+well_index;
            }
            $metadata_preview.load(url);
        };
    };
    
    // update tabs when tree selection changes or tabs switch
    $("#annotation_tabs").bind( "tabsshow", update_preview_tab);

    // on change of selection, update which tabs are enabled
    $("body").bind("selection_change.ome", function(event) {

        // clear contents of all panels
        $("#preview_tab").empty();

        var selected = $("body").data("selected_objects.ome");
        var orel = selected[0]["id"].split("-")[0];

        // we only care about changing selection if this tab is selected...
        var select_tab = $("#annotation_tabs").tabs( "option", "selected" );
        if (preview_tab_index == select_tab) {
            // we don't want to select this tab if multiple selected
            if (((orel!="image") && (orel != "well")) || (selected.length > 1)) {
                $("#annotation_tabs").tabs("select", 0);
            }
        }

        // update enabled state
        if((orel=="image" || orel=="well") && (selected.length == 1)) {
            $("#annotation_tabs").tabs("enable", preview_tab_index);
        } else {
            $("#annotation_tabs").tabs("disable", preview_tab_index);
        }

        // update tab content
        update_preview_tab();
    });
    
    // update after we've loaded the document
    update_preview_tab();
});

</script>