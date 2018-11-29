package eDoe.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import eDoe.models.Doador;
import eDoe.models.Receptor;
import eDoe.models.Usuario;
import eDoe.utils.Validador;

public class CrudUsuario {

	private Map<String, Usuario> usuarios = new LinkedHashMap<String, Usuario>();
	private GestorItem g = new GestorItem();

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Usuario ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void lerReceptores(String caminho) throws IOException {
		Scanner sc = new Scanner(new File(caminho));
		while (sc.hasNextLine()) {
			String[] dados = sc.nextLine().split(",");
			if (this.usuarios.containsKey(dados[0])) {
				Usuario u = new Receptor(dados[0], dados[1], dados[2], dados[3], dados[4], "receptor");
				this.usuarios.put(dados[0], u);
			}
		}
		sc.close();
	}

	public String adicionarDoador(String id, String nome, String email, String celular, String classe) {
		Validador.validadorAdicionaDoador(id, nome, email, celular, classe, this.usuarios);
		this.usuarios.put(id, new Doador(id, nome, email, celular, classe, "doador"));
		return id;
	}

	public String pesquisaUsuarioPorId(String id) {
		Validador.validadorPesquisaUsuarioPorId(id, this.usuarios);
		Usuario u = this.usuarios.get(id);
		return u.toString();
	}

	public String pesquisaUsuarioPorNome(String nome) {
		Validador.validadorPesquisaUsuarioPorNome(nome, this.usuarios);
		ArrayList<String> suporte = new ArrayList<>();
		String saida = "";
		for (Usuario u : this.usuarios.values()) {
			if (u.getNome().equals(nome))
				suporte.add(u.toString());
		}
		for (int i = 0; i < suporte.size() - 1; i++) {
			saida += suporte.get(i) + " | ";
		}

		return saida;
	}

	public void atualizaUsuario(String id, String nome, String email, String celular) {
		Validador.validadorAtualizaUsuario(id, nome, email, celular, this.usuarios);
		Usuario u = this.usuarios.get(id);
		u.setNome(nome);
		u.setEmail(email);
		u.setCelular(celular);
	}

	public void removeUsuario(String id) {
		Validador.validadorRemoveUsuario(id, this.usuarios);
		this.usuarios.remove(id);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Item ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void adicionarDescritor(String descricao) {
		g.adicionarDescritor(descricao);
	}

	public int adicionaItemParaDoacao(String idDoador, String descricao, int quantidade, String tags) {
		Usuario u = getUsuarioValido(idDoador, "doador");
		return g.adicionaItemParaDoacao(u, descricao, quantidade, tags);
	}

	public String exibeItem(int idItem, String idDoador) {
		Usuario u = getUsuarioValido(idDoador, "doador");
		return g.exibeItem(u, idItem);
	}

	public String atualizaItemParaDoacao(int idItem, String idDoador, int quantidade, String tags) {
		Usuario u = getUsuarioValido(idDoador, "doador");
		return g.atualizaItemParaDoacao(u, idItem, quantidade, tags);
	}

	public void removeItemParaDoacao(int idItem, String idDoador) {
		Usuario u = getUsuarioValido(idDoador, "doador");
		g.removeItemParaDoacao(u, idItem);
	}

	public String listaDescritorDeItensParaDoacao() {
		return g.listaDescritorDeItensParaDoacao();
	}

	public String listaItensParaDoacao() {
		return g.listaTodosOsItensExistentes(this.usuarios);
	}

	public String pesquisaItemParaDoacaoPorDescricao(String descricao) {
		return g.pesquisaItemParaDoacaoPorDescricao(descricao, this.usuarios);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Uteis ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private Usuario getUsuarioValido(String idUsuario, String status) {
		Validador.validadorParametro(idUsuario, "Entrada invalida: id do usuario nao pode ser vazio ou nulo.");
		if (!this.usuarios.containsKey(idUsuario))
			throw new IllegalArgumentException("Usuario nao encontrado: " + idUsuario + ".");
		if (!usuarios.get(idUsuario).getStatus().equals(status))
			throw new IllegalArgumentException("Usuario nao eh um " + status + ": " + idUsuario + ".");
		return this.usuarios.get(idUsuario);
	}

}