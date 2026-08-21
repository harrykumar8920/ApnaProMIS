package com.pams.config;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.hibernate.type.descriptor.java.LongJavaType;
import org.hibernate.internal.util.config.ConfigurationHelper;


public class CustomSequenceIdGenerator implements IdentifierGenerator {

    public static final String PREFIX_PARAM = "prefix";
    public static final String PREFIX_DEFAULT_PARAM = "";
    private String prefix;

    public static final String NUMBER_FORMAT_PARAM = "numberFormat";
    public static final String NUMBER_FORMAT_DEFAULT_PARAM = "%d";
    private String numberFormat;

    private org.hibernate.id.enhanced.SequenceStyleGenerator sequenceGenerator;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        // Generate the sequence number (cast to Serializable)
        Serializable generatedId = (Serializable) sequenceGenerator.generate(session, object);
        return prefix + String.format(numberFormat, generatedId);
    }

    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws HibernateException {
        // Initialize the sequence generator
        sequenceGenerator = new org.hibernate.id.enhanced.SequenceStyleGenerator();

        // Configuring the sequence generator (updated for Hibernate 6.x)
        sequenceGenerator.configure((Type) LongJavaType.INSTANCE, params, serviceRegistry);

        // Configure prefix and number format
        prefix = ConfigurationHelper.getString(PREFIX_PARAM, params, PREFIX_DEFAULT_PARAM);
        numberFormat = ConfigurationHelper.getString(NUMBER_FORMAT_PARAM, params, NUMBER_FORMAT_DEFAULT_PARAM);
    }
}
