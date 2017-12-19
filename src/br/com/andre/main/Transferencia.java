package br.com.andre.main;

import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.com.andre.modelo.Conta;

public class Transferencia {

	public static void main(String[] args) {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoPU");
		EntityManager em = emf.createEntityManager();
		
		Scanner entrada = new Scanner(System.in);
		
		Conta conta1 = em.find(Conta.class, 1L);
		if(conta1 == null){
			System.out.print("Digite o saldo inicial da conta1: ");
			Double saldoInicialConta1 = entrada.nextDouble();
			conta1 = new Conta();
			conta1.setSaldo(saldoInicialConta1);
		}
		
		Conta conta2 = em.find(Conta.class, 2L);
		if(conta2 == null){
			System.out.print("Digite o saldo inicial da conta2: ");
			Double saldoInicialConta2 = entrada.nextDouble();
			conta2 = new Conta();
			conta2.setSaldo(saldoInicialConta2);
		}
		
		em.getTransaction().begin();
		em.persist(conta1);
		em.persist(conta2);
		em.getTransaction().commit();
		
		System.out.println("Saldo da conta1 " + conta1.getSaldo() + 
				"\nSaldo da conta2 " + conta2.getSaldo());
		
		em.close();
		em = emf.createEntityManager();
		
		System.out.println("---------------------------");
		System.out.print("Digite o valor a retirar da conta 1 e depositar na conta 2: ");
		Double valorDaTransferencia = entrada.nextDouble();
		
		em.getTransaction().begin();
		conta1.setSaldo(conta1.getSaldo() - valorDaTransferencia);
		conta2.setSaldo(conta2.getSaldo() + valorDaTransferencia);
		
		em.merge(conta1);
		em.merge(conta2);
		if(conta1.getSaldo() >= 0){
			em.getTransaction().commit();
			System.out.println("Transferencia realzada com sucesso");
		}else{
			em.getTransaction().rollback();
			System.err.println("Transferencia não realizada");
		}
	} 
}