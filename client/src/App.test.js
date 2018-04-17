import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import {Jumbotron} from 'react-bootstrap';
import {shallow} from 'enzyme';

import { configure } from 'enzyme';
import Adapter from 'enzyme-adapter-react-16';

configure({ adapter: new Adapter() });



describe('App component', () => {
    describe('When dom mounted', () => {
        it('the component renders without crashing', () => {
          const div = document.createElement('div');
          ReactDOM.render(<App />, div);
          ReactDOM.unmountComponentAtNode(div);
        });
    });
    describe('When shallow rendered', () => {
        it('Then a Jumbotron is displayed', () => {
            let wrapper;
            wrapper = shallow(<App/>);
            expect(wrapper.find(Jumbotron).length).toBe(1);
        });
        it('Then a button with the label Create New Bookshelf is displayed', () => {
            let wrapper;
            wrapper = shallow(<App/>);
            expect(wrapper.find('#btnCreate').length).toBe(1);
            expect(wrapper.find('#btnCreate').props().children).toBe('Create New Bookshelf');
        });
    })
});
