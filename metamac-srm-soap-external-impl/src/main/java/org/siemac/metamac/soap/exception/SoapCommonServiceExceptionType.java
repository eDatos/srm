package org.siemac.metamac.soap.exception;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

// TODO put in common library if more soap services are created
public class SoapCommonServiceExceptionType {

    // Map
    protected static final Map<String, SoapCommonServiceExceptionType> LOOKUP              = new HashMap<String, SoapCommonServiceExceptionType>();

    // Error Codes
    public static final SoapCommonServiceExceptionType                 PARAMETER_REQUIRED  = create("exception.common.parameter.required");
    public static final SoapCommonServiceExceptionType                 PARAMETER_INCORRECT = create("exception.common.parameter.incorrect");
    public static final SoapCommonServiceExceptionType                 UNKNOWN             = create("exception.common.unknown");

    /**
     * Creates a new {@code CommonServiceExceptionType} or reuses an existing instance for a given
     * <var>code</var>.
     * 
     * @param code the commonServiceExceptionType code
     * @throws NullPointerException if <var>message</var> is {@code null} or <var>code</var> is {@code null}
     */
    protected static SoapCommonServiceExceptionType create(final String code) {
        final SoapCommonServiceExceptionType exceptionType;

        if (!LOOKUP.containsKey(code)) {
            exceptionType = new SoapCommonServiceExceptionType(code);
            LOOKUP.put(code, exceptionType);
        } else {
            exceptionType = LOOKUP.get(code);
        }

        return exceptionType;
    }

    public static SoapCommonServiceExceptionType lookup(final String code) {
        return LOOKUP.get(code);
    }

    // Attributes
    private String code = null;

    protected SoapCommonServiceExceptionType() {
    }

    protected SoapCommonServiceExceptionType(final String code) {
        if (code == null) {
            throw new NullPointerException();
        }

        this.code = code;
    }

    public String getCode() {
        return code;
    }

    /**
     * Returns a localized message for this reason type and locale.
     * 
     * @param locale The locale.
     * @return A localized message given a reason type and locale.
     */
    public String getMessageForReasonType(Locale locale) {

        String[] keys = this.getCode().split("\\.");
        String bundleName = "i18n/messages-soap-" + keys[1]; // The second part of the key is the message file name suffix.

        if (locale == null) {
            return ResourceBundle.getBundle(bundleName).getString(this.getCode());
        } else {
            return ResourceBundle.getBundle(bundleName, locale).getString(this.getCode());
        }
    }

    /**
     * Returns a message for this reason type in the default locale.
     * 
     * @return A message message for this reason type in the default locale.
     */
    public String getMessageForReasonType() {
        return getMessageForReasonType(null);
    }

    /**
     * Returns a lower case string of this enum.
     * 
     * @return a lower case string of this enum
     */
    public String lowerCaseString() {
        return this.toString().toLowerCase();
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Returns <em>"code"</em>.
     */
    @Override
    public String toString() {
        return code;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Considers only {@link #code} for equality.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (null == o) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }

        return code == ((SoapCommonServiceExceptionType) o).code;
    }

}