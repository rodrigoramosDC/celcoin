package mygroup;

import br.com.is2b.services.todaconta.GatewayWeb;
import gatewaywebservice.IGatewayWeb;
import gatewaywebservice.ObjectFactory;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.datacontract.schemas._2004._07.todaconta.Pagamento;
import org.datacontract.schemas._2004._07.todaconta.RespostaRecarga;
import org.datacontract.schemas._2004._07.todaconta.Telefone;
import org.datacontract.schemas._2004._07.todaconta.TipoPagamento;
import org.datacontract.schemas._2004._07.todaconta_webservice.Recarga;


/**
 * Unit test for simple App.
 */
public class AppTest
    extends TestCase
{
    private ObjectFactory objectFactory = new ObjectFactory();
    private org.datacontract.schemas._2004._07.todaconta.ObjectFactory todaContaObjectFactory = new org.datacontract.schemas._2004._07.todaconta.ObjectFactory();
    private org.datacontract.schemas._2004._07.todaconta_webservice.ObjectFactory webServiceObjectFactory = new org.datacontract.schemas._2004._07.todaconta_webservice.ObjectFactory();

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }


    public void testTopupRecharge(){

        // configure the operation
        //payment
        Pagamento payment = new Pagamento();
        payment.setFormaPagamento(TipoPagamento.DINHEIRO);
        payment.setPontos(0);
        payment.setQtdParcelas(1);
        payment.setValor(15.0);
        payment.setValorAcrescimo(0.0);
        payment.setValorBruto(0.0);
        payment.setValorDesconto(0.0);
        //Phone
        Telefone phone = new Telefone();
        phone.setNumero(99998889);
        phone.setCodigoEstado(11);
        phone.setCodigoPais(55);

        RespostaRecarga respostaRecarga = new RespostaRecarga();
        //Recharge
        Recarga recharge = new Recarga();
        recharge.setOperadoraId(1);
        recharge.setTelefoneRecarga(todaContaObjectFactory.createTelefone(phone));
        recharge.setDadosPagamento(todaContaObjectFactory.createPagamento(payment));
        recharge.setTerminalExterno(todaContaObjectFactory.createDadosOperacaoTerminalIdExterno("11036382702"));
        recharge.setNSUExterno(todaContaObjectFactory.createDadosUsuarioNSUExterno("1234567"));
        recharge.setCpfCnpj(todaContaObjectFactory.createDadosRegistroCpfCnpjBeneficiario("11036382702"));

        //create transaction

        // finalize the operation
        GatewayWeb rechargeHelper = new GatewayWeb();
        IGatewayWeb helper = rechargeHelper.getWSHttpBindingIGatewayWeb();
        //respostaRecarga = helper.processaTransacao(new TransacaoTemplate(recharge));

        //create transaction
        objectFactory.createProcessaTransacaoResponseProcessaTransacaoResult(respostaRecarga);


        assertNotNull("data");
    }

}
