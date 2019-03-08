package mygroup;

import br.com.is2b.services.todaconta.GatewayWeb;
import gatewaywebservice.IGatewayWeb;
import gatewaywebservice.ObjectFactory;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.datacontract.schemas._2004._07.todaconta.*;
import org.datacontract.schemas._2004._07.todaconta_webservice.*;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest
    extends TestCase
{
    private ObjectFactory objectFactory = new ObjectFactory();
    private org.datacontract.schemas._2004._07.todaconta.ObjectFactory todaContaObjectFactory = new org.datacontract.schemas._2004._07.todaconta.ObjectFactory();
    private org.datacontract.schemas._2004._07.todaconta_webservice.ObjectFactory webServiceObjectFactory = new org.datacontract.schemas._2004._07.todaconta_webservice.ObjectFactory();

    public QName stringToQElementName (String string){
        return new QName("http://GatewayWebService", string);
    }

    public JAXBElement<String> stringToElement(String elementXml, String value) {
        return new JAXBElement<String>(stringToQElementName(elementXml), String.class, Operadora.class, value);
    }
    /**
     * Create the test case
     *
     * @param testName name of the test casewe
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
        //Add CPF (Gov ID)
        JAXBElement<String> CPF = stringToElement("CpfCnpj","11036382702");

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


        RespostaRecarga respostaRecarga;

        //create transaction
//looks like "Point of Service"
        PontoDeAtendimento pa = new PontoDeAtendimento();
        pa.setLogin(todaContaObjectFactory.createPontoDeAtendimentoLogin("teste"));
        pa.setSenha(todaContaObjectFactory.createPontoDeAtendimentoSenha("teste"));
        pa.setPosId(1);

        DadosUsuario dadosUsuario = new DadosUsuario();
        dadosUsuario.setClienteId(12345);
        //dadosUsuario.setCodigoTransacao();
        dadosUsuario.setNSU(0);
        //dadosUsuario.setNSUExterno();
        //dadosUsuario.setPosId();
        //dadosUsuario.setTerminalExterno();

        //Recharge
        Recarga recharge = new Recarga();
        recharge.setOperadoraId(2087);
        recharge.setTelefoneRecarga(todaContaObjectFactory.createTelefone(phone));
        recharge.setDadosPagamento(todaContaObjectFactory.createPagamento(payment));
        recharge.setTerminalExterno(todaContaObjectFactory.createDadosOperacaoTerminalIdExterno("11036382702"));
        recharge.setNSUExterno(todaContaObjectFactory.createDadosUsuarioNSUExterno("1234567"));
        recharge.setCpfCnpj(CPF);
//        recharge.setCodigoAssinante();
//        recharge.setCodRegional();
//        recharge.setEnderecoIP();
//        recharge.setHashIntegracao();
//        recharge.setNSU();
//        recharge.setPontoAtendimento();
//        recharge.setSessionId();
//        recharge.setSiteIntegracaoId();
//        recharge.setTipoTransacao();
//        recharge.setToken();
//        recharge.setVersao();




        //TransacaoTemplate transacaoTemplate = new TransacaoFinanceira();
        TransacaoFinanceira transacaoFinanceira = new TransacaoFinanceira();
        transacaoFinanceira.setCpfCnpj(CPF);
        transacaoFinanceira.setTerminalExterno(todaContaObjectFactory.createDadosOperacaoTerminalIdExterno("11036382702"));
        transacaoFinanceira.setPontoAtendimento(todaContaObjectFactory.createPontoDeAtendimento(pa));
        transacaoFinanceira.setEnderecoIP(stringToElement("enderecoIP", "127.0.0.1"));
        transacaoFinanceira.setDadosPagamento(todaContaObjectFactory.createPagamento(payment));
//       transacaoTemplate.setHashIntegracao();
        transacaoFinanceira.setNSU(0);
        transacaoFinanceira.setTipoTransacao(stringToElement("tipotransacao", "RECARGA"));
        transacaoFinanceira.setNSUExterno(todaContaObjectFactory.createDadosUsuarioNSUExterno("1234567"));

        // finalize the operation
        GatewayWeb rechargeHelper = new GatewayWeb();
        IGatewayWeb helper = rechargeHelper.getBasicHttpBindingIGatewayWeb();

        Resposta resposta = helper.processaTransacao(transacaoFinanceira);

        System.out.println("Server responded with the message: - " + resposta.getMensagemErro().getValue());
        System.out.println("Server responded with the Code: - " + resposta.getCodigoErro().getValue());

        assertTrue("Server need to answer with the code 000", resposta.getCodigoErro().getValue() == "000");
    }

    public void testTopupConsultPhoneProvider() {

        //looks like "Point of Service"
        PontoDeAtendimento pa = new PontoDeAtendimento();
        pa.setLogin(todaContaObjectFactory.createPontoDeAtendimentoLogin("teste"));
        pa.setSenha(todaContaObjectFactory.createPontoDeAtendimentoSenha("teste"));
        pa.setPosId(1);

        //Add CPF (Gov ID)
        JAXBElement<String> CPF = stringToElement("CpfCnpj","11036382702");

        Integer DDD = 11;

        TransacaoConsultaOperadoraDDD transacaoConsultaOperadoraDDD = new TransacaoConsultaOperadoraDDD();
        transacaoConsultaOperadoraDDD.setCpfCnpj(CPF);
        transacaoConsultaOperadoraDDD.setDdd(DDD);

        transacaoConsultaOperadoraDDD.setCategoriaRecarga(CategoriaRecarga.TODOS);
        transacaoConsultaOperadoraDDD.setPontoAtendimento(todaContaObjectFactory.createPontoDeAtendimento(pa));
        transacaoConsultaOperadoraDDD.setTipoRecarga(TipoRecarga.TODOS);

        //Transaction Type = Consulta Operadora DDD (phone provider consult)
        transacaoConsultaOperadoraDDD.setTipoTransacao(stringToElement("TipoTransacao","CONSULTAOPERADORADDD"));
        transacaoConsultaOperadoraDDD.setEnderecoIP(stringToElement("EnderecoIP","127.0.0.1"));

        //invoke the server
        GatewayWeb ss = new GatewayWeb();
        IGatewayWeb port = ss.getBasicHttpBindingIGatewayWeb();
        RespostaConsultaOperadora resp;

        System.out.println("Invoking transaction ... "+ port.toString());

        //Process transaction
        port.processaToken(new BaseToken());
        port.validaTiket("12345");
        transacaoConsultaOperadoraDDD.setCpfCnpj(CPF);
        transacaoConsultaOperadoraDDD.setPontoAtendimento(todaContaObjectFactory.createPontoDeAtendimento(pa));
        resp = (RespostaConsultaOperadora) port.processaTransacao(transacaoConsultaOperadoraDDD);
        JAXBElement<ArrayOfOperadora> ro = resp.getOperadoras();
        ArrayOfOperadora arrayOfOperadora = ro.getValue();

        List<Operadora> operadoras = arrayOfOperadora.getOperadora();

        System.out.println("Server responded with the message: - " + resp.getMensagemErro().getValue());
        System.out.println("Server responded with the Code: - " + resp.getCodigoErro().getValue());

        //TODO Server answered with the message: - Objeto nulo ou necessario nao recebido (Null Object or Needed not received)
        //TODO Server answered with the Code: - 999

        assertTrue("the Server need to answer with the code 000", resp.getCodigoErro().getValue() == "000");

    }

    public void testTopupConsultValuesOfProvider()  {
        assertNotNull("data");
    }

}
