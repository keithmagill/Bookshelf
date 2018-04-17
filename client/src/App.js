import React, { Component } from 'react';
import { Jumbotron, Button } from 'react-bootstrap';

class App extends Component {
  render() {
    return (
      <div className="container-fluid">
          <Jumbotron>
              <h1>Bookshelf</h1>
              <h2>Create your own virtual library</h2>
              <p>To get started simply create a new bookshelf repository and commence adding books</p>
              <div>
                <Button id="btnCreate" bsStyle="primary">Create New Bookshelf</Button>
              </div>
          </Jumbotron>
      </div>
    );
  }
}

export default App;
