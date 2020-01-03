package com.gasstations.gastation;

public class Posto {
		
	int id;
	float latitude;
	float longitude;
	String endereco;
	String bandeira;
	String nome;
	String formaDePagamento;
	Float precoGasolina;
	Float precoEtanol;
	Float precoDiesel;
	String adicionais;
	String horarioDeFuncionamento;
	
	public Posto(){}
	
	public Posto(int id, float latitude, float longitude, String endereco,
			String bandeira, String formaDePagamento, Float precoGasolina,
			Float precoEtanol, Float precoDiesel, String adicionais,String horarioDeFuncionamento) {
		super();
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.endereco = endereco;
		this.bandeira = bandeira;
		this.formaDePagamento = formaDePagamento;
		this.precoGasolina = precoGasolina;
		this.precoEtanol = precoEtanol;
		this.precoDiesel = precoDiesel;
		this.adicionais = adicionais;
		this.horarioDeFuncionamento=horarioDeFuncionamento;
	}
	
	
}

