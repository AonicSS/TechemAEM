import messagesDE from './i18n/de.flat.json';

import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import { createStore } from 'redux';
import { Provider } from 'react-redux';
import { composeWithDevTools } from 'redux-devtools-extension';
import AppStore from './redux/index';
import { install } from 'redux-loop';
import { IntlProvider } from 'react-intl';

const store = createStore(AppStore as any, composeWithDevTools(install()));

ReactDOM.render(
	<React.StrictMode>
		<IntlProvider locale="de-DE" messages={messagesDE}>
			<Provider store={store}>
				<App />
			</Provider>
		</IntlProvider>
	</React.StrictMode>,
	document.getElementById('techem-rwm')
);
