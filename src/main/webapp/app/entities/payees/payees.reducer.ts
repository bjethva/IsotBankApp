import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPayees, defaultValue } from 'app/shared/model/payees.model';

export const ACTION_TYPES = {
  FETCH_PAYEES_LIST: 'payees/FETCH_PAYEES_LIST',
  FETCH_PAYEES: 'payees/FETCH_PAYEES',
  CREATE_PAYEES: 'payees/CREATE_PAYEES',
  UPDATE_PAYEES: 'payees/UPDATE_PAYEES',
  DELETE_PAYEES: 'payees/DELETE_PAYEES',
  RESET: 'payees/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPayees>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type PayeesState = Readonly<typeof initialState>;

// Reducer

export default (state: PayeesState = initialState, action): PayeesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PAYEES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PAYEES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PAYEES):
    case REQUEST(ACTION_TYPES.UPDATE_PAYEES):
    case REQUEST(ACTION_TYPES.DELETE_PAYEES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PAYEES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PAYEES):
    case FAILURE(ACTION_TYPES.CREATE_PAYEES):
    case FAILURE(ACTION_TYPES.UPDATE_PAYEES):
    case FAILURE(ACTION_TYPES.DELETE_PAYEES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PAYEES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PAYEES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PAYEES):
    case SUCCESS(ACTION_TYPES.UPDATE_PAYEES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PAYEES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/payees';

// Actions

export const getEntities: ICrudGetAllAction<IPayees> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PAYEES_LIST,
  payload: axios.get<IPayees>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IPayees> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PAYEES,
    payload: axios.get<IPayees>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPayees> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PAYEES,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPayees> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PAYEES,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPayees> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PAYEES,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
