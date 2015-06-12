package com.lfa.constants;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Classe Symbol. Representa um símbolo do alfabeto.
 */
@Data
@EqualsAndHashCode(callSuper = false, of = "str")
@ToString(callSuper = false, of = "str")
public final class Symbol {

	private final String str;

	/**
	 * Instancia um novo Symbol. Construtor protegido para uso somente por
	 * classes do pacote.
	 *
	 * @param str
	 *            A string que representa símbolo gráfico.
	 */
	protected Symbol(String str) {
		this.str = str;
	}

}
