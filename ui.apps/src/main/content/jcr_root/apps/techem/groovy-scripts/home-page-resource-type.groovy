// Changes all home page resource types to correct one (techem/components/structure/home-page)
def queryManager = session.workspace.queryManager;
def sqlQuery = "select [jcr:path], * from [nt:unstructured] as a where [cq:template] = '/conf/techem/settings/wcm/templates/home-page' and isdescendantnode(a, '/content/techem')";
def queryObj = queryManager.createQuery(sqlQuery, 'sql');
def queryResults = queryObj.execute();
def renamedCt = 0;

queryResults.nodes.each {
    node ->
        String nodePath = node.path;
        String resType = node.get("sling:resourceType");

        if (resType.equals("techem/components/page")) {
            println nodePath;
            node.set("sling:resourceType","techem/components/structure/home-page");
            node.getSession().save();
            renamedCt++;
        }
}
if(renamedCt == 0){
    println "No node needs update !!"
}
println "Renamed in total: ${renamedCt}"