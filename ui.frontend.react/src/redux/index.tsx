import { combineReducers } from 'redux-loop';
import AppData from './reducers/App';

const AppStore = combineReducers({ appData: AppData });

export default AppStore;
