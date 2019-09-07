import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Payees from './payees';
import PayeesDetail from './payees-detail';
import PayeesUpdate from './payees-update';
import PayeesDeleteDialog from './payees-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PayeesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PayeesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PayeesDetail} />
      <ErrorBoundaryRoute path={match.url} component={Payees} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PayeesDeleteDialog} />
  </>
);

export default Routes;
