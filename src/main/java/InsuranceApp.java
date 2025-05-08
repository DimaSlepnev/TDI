import openllet.jena.PelletReasonerFactory;
import org.apache.jena.ontology.*;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;

public class InsuranceApp {
    // SPARQL-запит для отримання суми компенсації
    private static final String APPROVAL_QUERY = """
            PREFIX ex: <http://www.w3.org/2002/07/insurance#>
            SELECT ?case ?sum WHERE {
              ?case a ex:СТРАХОВИЙ_ВИПАДОК .
              ?case ex:отримуєКомпенсацію ?sum .
            }
        """;
    // SPARQL-запит для отримання відмови компенсації
    private static final String REFUSAL_QUERY = """
            PREFIX ex: <http://www.w3.org/2002/07/insurance#>
            SELECT ?case ?refuse WHERE {
              ?case a ex:СТРАХОВИЙ_ВИПАДОК .
              ?case ex:відмоваУВиплаті ?refuse .
            }
        """;


    public static void main(String[] args) {

        // Завантаження онтології
        OntModel baseModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        baseModel.read("src/main/resources/insurance_turtle.ttl", null, "TURTLE");

        // Застосування Pellet Reasoner
        OntModel infModel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC, baseModel);


        Query query = QueryFactory.create(APPROVAL_QUERY);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, infModel)) {
            ResultSet results = qexec.execSelect();
            ResultSetFormatter.out(System.out, results, query);
        }

        try (QueryExecution qexec = QueryExecutionFactory.create(REFUSAL_QUERY, infModel)) {
            ResultSet results = qexec.execSelect();
            ResultSetFormatter.out(System.out, results, query);
        }
    }
}
