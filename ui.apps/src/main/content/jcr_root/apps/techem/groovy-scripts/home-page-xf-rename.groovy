// renames exp. fragment node names to experiencefragment-header and experiencefragment-footer
def queryManager = session.workspace.queryManager;
def sqlQuery = "select [jcr:path], * from [nt:unstructured] as a where [cq:template] = '/conf/techem/settings/wcm/templates/home-page' and isdescendantnode(a, '/content/techem')";
def queryObj = queryManager.createQuery(sqlQuery, 'sql');
def queryResults = queryObj.execute();
def renamedCt = 0;

queryResults.nodes.each {
    node ->
        String nodePath = node.path;
        // HEADER EXP FRAGMENT
        if (node.hasNode("experiencefragment")) {
            def nodeItr =node.getNodes();
            def navId="";
            def navName="";
            nodeItr.each{
                navTabsNode ->
                    // rename exp footer
                    if (navTabsNode.getName().equals("experiencefragment")) {
                        println "Node at path = " + nodePath + " needs update (HEADER)"
                        navTabsNode.set("fragmentVariationPath","");
                        def srcPath = nodePath + "/" + "experiencefragment";
                        def destPath = nodePath + "/" + "experiencefragment-header";
                        node.getSession().move(srcPath, destPath); // move method from Session API is used for renaming the node.
                        node.getSession().save();
                        renamedCt = renamedCt + 1;
                        println srcPath + " renamed to " + destPath;
                    }
            }
        }
        // FOOTER EXP FRAGMENT
        if (node.hasNode("experiencefragment_2088209270")) {
            def nodeItr =node.getNodes();
            def navId="";
            def navName="";
            nodeItr.each{
                navTabsNode ->
                    // rename exp footer
                    if (navTabsNode.getName().equals("experiencefragment_2088209270")) {
                        println "Node at path = " + nodePath + " needs update (FOOTER)"
                        navTabsNode.set("fragmentVariationPath","");
                        def srcPath = nodePath + "/" + "experiencefragment_2088209270";
                        def destPath = nodePath + "/" + "experiencefragment-footer";
                        node.getSession().move(srcPath, destPath); // move method from Session API is used for renaming the node.
                        node.getSession().save();
                        renamedCt = renamedCt + 1;
                        println srcPath + " renamed to " + destPath;
                    }
            }
        }

}
if(renamedCt == 0){
    println "No node needs update !!"
}
println "Renamed in total: ${renamedCt}"