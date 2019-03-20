package mygroup;

import br.com.is2b.services.todaconta.GatewayWeb;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import gatewaywebservice.IGatewayWeb;
import gatewaywebservice.ObjectFactory;
import javafx.beans.binding.BooleanExpression;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import okhttp3.*;
import org.datacontract.schemas._2004._07.todaconta.*;
import org.datacontract.schemas._2004._07.todaconta_webservice.*;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.io.IOException;
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

    public void testTopupConsultPhoneProviderUsingOkHttp() throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType JSON
                = MediaType.parse("text/xml");

        String bodyString =
                "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "    <s:Body>\n" +
                "        <ProcessaTransacaoResponse xmlns=\"http://GatewayWebService\">\n" +
                "            <ProcessaTransacaoResult i:type=\"a:RespostaConsultaValoresOperadora\" xmlns:a=\"http://schemas.datacontract.org/2004/07/TodaConta.WebService\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "                <a:CodigoErro>000</a:CodigoErro>\n" +
                "                <a:MensagemErro>SUCESSO</a:MensagemErro>\n" +
                "                <a:StatusTransacao>SUCESSO</a:StatusTransacao>\n" +
                "                <a:ProtocoloIdConsulta>0</a:ProtocoloIdConsulta>\n" +
                "                <a:Valores>\n" +
                "                    <a:ValorRecarga>\n" +
                "                        <a:Caracteristicas i:nil=\"true\"/>\n" +
                "                        <a:CodigoProduto>0</a:CodigoProduto>\n" +
                "                        <a:CustoRecarga>0</a:CustoRecarga>\n" +
                "                        <a:DetalheProduto/>\n" +
                "                        <a:NomeProduto>R$ 10,00</a:NomeProduto>\n" +
                "                        <a:ProdutoChecksum>-2147483640</a:ProdutoChecksum>\n" +
                "                        <a:ValidadeProduto>30</a:ValidadeProduto>\n" +
                "                        <a:ValorBonus>0</a:ValorBonus>\n" +
                "                        <a:ValorMax>10</a:ValorMax>\n" +
                "                        <a:ValorMin>10</a:ValorMin>\n" +
                "                    </a:ValorRecarga>\n" +
                "                    <a:ValorRecarga>\n" +
                "                        <a:Caracteristicas i:nil=\"true\"/>\n" +
                "                        <a:CodigoProduto>0</a:CodigoProduto>\n" +
                "                        <a:CustoRecarga>0</a:CustoRecarga>\n" +
                "                        <a:DetalheProduto/>\n" +
                "                        <a:NomeProduto>R$ 13,00</a:NomeProduto>\n" +
                "                        <a:ProdutoChecksum>-2147483640</a:ProdutoChecksum>\n" +
                "                        <a:ValidadeProduto>30</a:ValidadeProduto>\n" +
                "                        <a:ValorBonus>0</a:ValorBonus>\n" +
                "                        <a:ValorMax>13</a:ValorMax>\n" +
                "                        <a:ValorMin>13</a:ValorMin>\n" +
                "                    </a:ValorRecarga>\n" +
                "                    <a:ValorRecarga>\n" +
                "                        <a:Caracteristicas i:nil=\"true\"/>\n" +
                "                        <a:CodigoProduto>0</a:CodigoProduto>\n" +
                "                        <a:CustoRecarga>0</a:CustoRecarga>\n" +
                "                        <a:DetalheProduto/>\n" +
                "                        <a:NomeProduto>R$ 15,00</a:NomeProduto>\n" +
                "                        <a:ProdutoChecksum>-2147483640</a:ProdutoChecksum>\n" +
                "                        <a:ValidadeProduto>30</a:ValidadeProduto>\n" +
                "                        <a:ValorBonus>0</a:ValorBonus>\n" +
                "                        <a:ValorMax>15</a:ValorMax>\n" +
                "                        <a:ValorMin>15</a:ValorMin>\n" +
                "                    </a:ValorRecarga>\n" +
                "                    <a:ValorRecarga>\n" +
                "                        <a:Caracteristicas i:nil=\"true\"/>\n" +
                "                        <a:CodigoProduto>0</a:CodigoProduto>\n" +
                "                        <a:CustoRecarga>0</a:CustoRecarga>\n" +
                "                        <a:DetalheProduto/>\n" +
                "                        <a:NomeProduto>R$ 20,00 + 2</a:NomeProduto>\n" +
                "                        <a:ProdutoChecksum>-2147483640</a:ProdutoChecksum>\n" +
                "                        <a:ValidadeProduto>60</a:ValidadeProduto>\n" +
                "                        <a:ValorBonus>0</a:ValorBonus>\n" +
                "                        <a:ValorMax>20</a:ValorMax>\n" +
                "                        <a:ValorMin>20</a:ValorMin>\n" +
                "                    </a:ValorRecarga>\n" +
                "                    <a:ValorRecarga>\n" +
                "                        <a:Caracteristicas i:nil=\"true\"/>\n" +
                "                        <a:CodigoProduto>0</a:CodigoProduto>\n" +
                "                        <a:CustoRecarga>0</a:CustoRecarga>\n" +
                "                        <a:DetalheProduto/>\n" +
                "                        <a:NomeProduto>R$ 30,00 + 4</a:NomeProduto>\n" +
                "                        <a:ProdutoChecksum>-2147483640</a:ProdutoChecksum>\n" +
                "                        <a:ValidadeProduto>90</a:ValidadeProduto>\n" +
                "                        <a:ValorBonus>0</a:ValorBonus>\n" +
                "                        <a:ValorMax>30</a:ValorMax>\n" +
                "                        <a:ValorMin>30</a:ValorMin>\n" +
                "                    </a:ValorRecarga>\n" +
                "                    <a:ValorRecarga>\n" +
                "                        <a:Caracteristicas i:nil=\"true\"/>\n" +
                "                        <a:CodigoProduto>0</a:CodigoProduto>\n" +
                "                        <a:CustoRecarga>0</a:CustoRecarga>\n" +
                "                        <a:DetalheProduto/>\n" +
                "                        <a:NomeProduto>R$ 35,00 + 5</a:NomeProduto>\n" +
                "                        <a:ProdutoChecksum>-2147483640</a:ProdutoChecksum>\n" +
                "                        <a:ValidadeProduto>90</a:ValidadeProduto>\n" +
                "                        <a:ValorBonus>0</a:ValorBonus>\n" +
                "                        <a:ValorMax>35</a:ValorMax>\n" +
                "                        <a:ValorMin>35</a:ValorMin>\n" +
                "                    </a:ValorRecarga>\n" +
                "                    <a:ValorRecarga>\n" +
                "                        <a:Caracteristicas i:nil=\"true\"/>\n" +
                "                        <a:CodigoProduto>0</a:CodigoProduto>\n" +
                "                        <a:CustoRecarga>0</a:CustoRecarga>\n" +
                "                        <a:DetalheProduto/>\n" +
                "                        <a:NomeProduto>R$ 40,00 + 6</a:NomeProduto>\n" +
                "                        <a:ProdutoChecksum>-2147483640</a:ProdutoChecksum>\n" +
                "                        <a:ValidadeProduto>90</a:ValidadeProduto>\n" +
                "                        <a:ValorBonus>0</a:ValorBonus>\n" +
                "                        <a:ValorMax>40</a:ValorMax>\n" +
                "                        <a:ValorMin>40</a:ValorMin>\n" +
                "                    </a:ValorRecarga>\n" +
                "                    <a:ValorRecarga>\n" +
                "                        <a:Caracteristicas i:nil=\"true\"/>\n" +
                "                        <a:CodigoProduto>0</a:CodigoProduto>\n" +
                "                        <a:CustoRecarga>0</a:CustoRecarga>\n" +
                "                        <a:DetalheProduto/>\n" +
                "                        <a:NomeProduto>R$ 50,00 + 8</a:NomeProduto>\n" +
                "                        <a:ProdutoChecksum>-2147483640</a:ProdutoChecksum>\n" +
                "                        <a:ValidadeProduto>150</a:ValidadeProduto>\n" +
                "                        <a:ValorBonus>0</a:ValorBonus>\n" +
                "                        <a:ValorMax>50</a:ValorMax>\n" +
                "                        <a:ValorMin>50</a:ValorMin>\n" +
                "                    </a:ValorRecarga>\n" +
                "                    <a:ValorRecarga>\n" +
                "                        <a:Caracteristicas i:nil=\"true\"/>\n" +
                "                        <a:CodigoProduto>0</a:CodigoProduto>\n" +
                "                        <a:CustoRecarga>0</a:CustoRecarga>\n" +
                "                        <a:DetalheProduto/>\n" +
                "                        <a:NomeProduto>R$ 100,00 + 18</a:NomeProduto>\n" +
                "                        <a:ProdutoChecksum>-2147483640</a:ProdutoChecksum>\n" +
                "                        <a:ValidadeProduto>150</a:ValidadeProduto>\n" +
                "                        <a:ValorBonus>0</a:ValorBonus>\n" +
                "                        <a:ValorMax>100</a:ValorMax>\n" +
                "                        <a:ValorMin>100</a:ValorMin>\n" +
                "                    </a:ValorRecarga>\n" +
                "                </a:Valores>\n" +
                "            </ProcessaTransacaoResult>\n" +
                "        </ProcessaTransacaoResponse>\n" +
                "    </s:Body>\n" +
                "</s:Envelope>";
        RequestBody body = RequestBody.create(JSON, bodyString);
        String url = "http://hmlgtodaconta.is2b.com.br:54003/TodaConta/WebService/BasicBinding";
        Request request = new Request.Builder()
                .url(url)
                .addHeader("content-type", "text/xml")
                .addHeader("SOAPAction", "http://GatewayWebService/IGatewayWeb/ProcessaTransacao")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        String responseString = response.body().string();
        System.out.println("Status code: " + response.code());
        System.out.println("Response from the server: " + responseString);
        assertNotNull(responseString);
        assertTrue(response.code() == 200);


    }

    public void testTopupWithUnirest() throws UnirestException {
        String body = "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "   <s:Body>\n" +
                "      <ProcessaTransacao xmlns=\"http://GatewayWebService\">\n" +
                "         <transacao i:type=\"a:TransacaoConsultaValoresOperadoraDDD\" xmlns:a=\"http://schemas.datacontract.org/2004/07/TodaConta.WebService.Transacoes\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "            <a:CpfCnpj>11036382702</a:CpfCnpj>\n" +
                "            <a:PontoAtendimento xmlns:b=\"http://schemas.datacontract.org/2004/07/TodaConta.WebService\">\n" +
                "               <b:Login>teste</b:Login>               \n" +
                "               <b:Senha>teste</b:Senha>\n" +
                "            </a:PontoAtendimento>                       \n" +
                "            <a:TipoTransacao>CONSULTAVALORESOPERADORADDD</a:TipoTransacao>\n" +
                "            <a:OperadoraId>2087</a:OperadoraId>            \n" +
                "            <a:ddd>11</a:ddd>            \n" +
                "         </transacao>\n" +
                "      </ProcessaTransacao>\n" +
                "   </s:Body>\n" +
                "</s:Envelope>";
        HttpResponse<String> response = Unirest.post("http://hmlgtodaconta.is2b.com.br:54003/TodaConta/WebService/BasicBinding")
                .header("content-type", "text/xml")
                .header("SOAPAction", "http://GatewayWebService/IGatewayWeb/ProcessaTransacao")
                .body(body)
                .asString();
        System.out.println("Status code: " + response.getStatus());
        System.out.println("Response from the server: " + response.getBody());
        assertTrue(response.getStatus() == 200);
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

        TransacaoConsultaValoresOperadoraDDD transacaoConsultaOperadoraDDD = new TransacaoConsultaValoresOperadoraDDD();
        transacaoConsultaOperadoraDDD.setCpfCnpj(CPF);
        transacaoConsultaOperadoraDDD.setDdd(DDD);

        transacaoConsultaOperadoraDDD.setPontoAtendimento(todaContaObjectFactory.createPontoDeAtendimento(pa));

        //Transaction Type = Consulta Operadora DDD (phone provider consult)
        transacaoConsultaOperadoraDDD.setTipoTransacao(stringToElement("TipoTransacao","CONSULTAOPERADORADDD"));
        transacaoConsultaOperadoraDDD.setEnderecoIP(stringToElement("EnderecoIP","127.0.0.1"));

        //invoke the server
        GatewayWeb ss = new GatewayWeb();
        IGatewayWeb port = ss.getBasicHttpBindingIGatewayWeb();
        RespostaConsultaValoresOperadora resp;

        System.out.println("Invoking transaction ... "+ port.toString());

        //Process transaction
        port.processaToken(new BaseToken());
        port.validaTiket("12345");
        transacaoConsultaOperadoraDDD.setCpfCnpj(CPF);
        transacaoConsultaOperadoraDDD.setPontoAtendimento(todaContaObjectFactory.createPontoDeAtendimento(pa));
        resp = (RespostaConsultaValoresOperadora) port.processaTransacao(transacaoConsultaOperadoraDDD);

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
