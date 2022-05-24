package com.letscode.modeloentrega.service;

import com.letscode.modeloentrega.domain.Cliente;
import com.letscode.modeloentrega.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService implements iClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Integer calcularIdadeCliente(Integer codigo) {

        Cliente cliente = clienteRepository.findById(codigo).get();

        /*
            URL: http://localhost:8080/v1/clientes/calcular_idade?codigo=1

            Construa a lógica de código necessária para responder quantos anos o cliente possui.

            Considere que o código acima já está retornando o cliente correto da base de dados, sendo necessário
            apenas desenvolver a lógica de cálculo para responder sua idade.

            O retorno deste método deverá ser apenas um integer que represente a idade deste cliente.
         */

        return (int) ChronoUnit.YEARS.between(cliente.getDataNascimento(), LocalDate.now());
    }

    @Override
    public List<String> listarNomesClientes() {

        List<Cliente> listaClientes = clienteRepository.findAll();

        /*
            URL: http://localhost:8080/v1/clientes/listar

            Refatore o método abaixo para gerar a mesma saída, porém sem o uso de estruturas de repetição (for, while, etc.)

            Saída esperada: uma lista contendo APENAS os nomes dos clientes e com todos os seus valores em letras maiúsculas.

            Considere que a consulta acima já retorna uma lista completa com todos os clientes existentes.
         */

        List<String> listaNomes = new ArrayList<>();

//        for (int i = 0; i < listaClientes.size(); i++) {
//            listaNomes.add(listaClientes.get(i).getNome().toUpperCase());
//        }
        listaNomes = listaClientes.stream().map(x -> x.getNome().toUpperCase())
        							.collect(Collectors.toList());
        return listaNomes;
    }

    @Override
    public String consultarNomeCliente(Integer codigo) {
        /*
            URL: http://localhost:8080/v1/clientes/consultar_nome?codigo=9999

            Refatore o código abaixo para garantir a boa prática de evitar retornos nulos ou excecoes por dados null;

            Cenário esperado: quando o código informado for válido (existir um cliente com aquele código), deverá ser retornado
            o nome do cliente encontrado. Caso contrário, devolver uma mensagem de erro avisando que não existe ninguém com o código informado.
         */

     /*   Cliente cliente = clienteRepository.findById(codigo).get();

        return cliente.getNome();
     */
    	Optional <Cliente> optionalCliente = clienteRepository.findById(codigo);
    	
    	if (optionalCliente.isPresent()) {
    		return optionalCliente.get().getNome();
    	} else {
    		return "Cliente não encontrado";
    	}
    }

    @Override
    public List<Cliente> listarClientesContato() {

        List<Cliente> listaClientes = clienteRepository.findAll();

        /*
            URL: http://localhost:8080/v1/clientes/listar_mulheres

            A partir da lista de clientes obtida pelo codigo acima, devolva apenas aqueles clientes que se encaixarem no perfil abaixo:
                - gênero FEMININO (F)
                - idade maior ou igual a 30 anos
                - quantidade máxima de clientes na lista: 3
         */
        
        return listaClientes.stream().filter (x -> x.getGenero().equals('F') && 
        		ChronoUnit.YEARS.between(x.getDataNascimento(), LocalDate.now()) >= 30)
        					.limit (3).collect(Collectors.toList());

       // return listaClientes;
    }

    @Override
    public List<Cliente> listaQuebrada() {

        /*
            URL: http://localhost:8080/v1/clientes/bug

            Este método deveria listar todos os dados dos clientes ordenados em ordem decrescente pela quantidade de visitas limitados em até 3 resultados,
            porém não é isso que está acontecendo. Verifique se as implementações abaixo estão corretas e, se necessário, faça as modificações que julgar apropriadas.
         */

        List<Cliente> listaClientes = clienteRepository.findAll();

      /*  listaClientes.stream().skip(3)
                .sorted(Comparator.comparingInt(Cliente::getQuantidadeVisistas))
                .map(x -> { return new Cliente(listaClientes.get(0).getCodigo(), listaClientes.get(0).getNome(), listaClientes.get(0).getDataNascimento(), listaClientes.get(0).getGenero(), listaClientes.get(0).getQuantidadeVisistas()); })
                .collect(Collectors.toList());
	  */
       return listaClientes.stream().sorted(Comparator.comparingInt(Cliente::getQuantidadeVisistas).reversed())
        						.limit(3)
        						.collect(Collectors.toList());
        
    }
}
