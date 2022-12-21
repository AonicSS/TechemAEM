/**
 * Helper to translate messages via intl
 *
 * @param   {Object}  intl        The intl object, mostly from "injectIntl" HOC of "react-intl"
 * @param   {String}  messageKey  The message key like "components.login.headline"
 * @param   {Object}  params      Params for intl which will be replaced within the message source
 *
 * @return  {String}              The translated message
 */
const Translate = (intl: any, messageKey: string, params: any = {}) => {
	return intl.formatMessage(
		{
			id: messageKey,
		},
		params
	);
};

export default Translate;
