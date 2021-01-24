package com.ivini.saidasjuntas.acesso.validacao;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;

public class MesmosValoresValidator implements ConstraintValidator<MesmosValores, Object> {
	private String primeiroCampo;
	private String segundoCampo;
	private String mensagem;

	@Override
	public void initialize(final MesmosValores anotacao) {
		this.primeiroCampo = anotacao.primeiro();
		this.segundoCampo = anotacao.segundo();
		this.mensagem = anotacao.message();
	}
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
	    boolean valid = true;
        try
        {
            final Object firstObj = BeanUtils.getProperty(value, primeiroCampo);
            final Object secondObj = BeanUtils.getProperty(value, segundoCampo);

            valid =  firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);
        }
        catch (final Exception ignore)
        {
            // ignore
        }

        if (!valid){
            context.buildConstraintViolationWithTemplate(mensagem)
                    .addPropertyNode(segundoCampo)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return valid;
	}

}
