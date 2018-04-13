/**
 * History:
 * 
 * 1 (2018-04-13) - Initial release.
 */
package pl.waw.medynski.ws;




/**
 * Imports.
 */
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;




/**
 * TestWebService class.
 * 
 * @author Maciej Medyński
 * @version 1
 */
@WebService(serviceName = "TestWebService")
@HandlerChain(file = "TestWebService_handler.xml")
public class TestWebService
{
    /**
     * This is a sample web service operation
     * @param txt
     * @return 
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt)
    {
        return "Hello " + txt + " !";
    }
}
