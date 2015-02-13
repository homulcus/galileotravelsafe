/*
 * Copyright 2013 Samuel Franklyn <sfranklyn@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package galileonews.jsf.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import javax.faces.model.DataModel;
import javax.faces.model.DataModelEvent;
import javax.faces.model.DataModelListener;
import galileonews.ejb.datamodel.DataModelDao;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
public class DatabaseDataModel extends DataModel implements Serializable {

    private static final Logger log = Logger.getLogger(DatabaseDataModel.class.getName());
    private static final long serialVersionUID = -5162660797984932492L;
    private DataModelDao dataModel = null;
    private final AtomicInteger rowIndex = new AtomicInteger(-1);
    private String select = null;
    private String selectCount = null;
    private Map selectParam = null;

    @Override
    @SuppressWarnings("unchecked")
    public int getRowCount() {
        if (getDataModel() == null) {
            return -1;
        }
        try {
            return getDataModel().getAllCount(selectCount, selectParam).intValue();
        } catch (Exception ex) {
            log.severe(ex.toString());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean isRowAvailable() {
        if ((getDataModel() == null) || rowIndex.get() < 0) {
            return false;
        }
        return rowIndex.get() <= (getRowCount() - 1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object getRowData() {
        if (getDataModel() == null) {
            return null;
        }
        if (!isRowAvailable()) {
            final String errMsg = "Row is unavalaible";
            throw new IllegalArgumentException(errMsg);
        }
        final List list;
        try {
            list = getDataModel().getAll(select, selectParam, rowIndex.get(), 1);
        } catch (Exception ex) {
            log.severe(ex.toString());
            throw new RuntimeException(ex);
        }
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public int getRowIndex() {
        return rowIndex.get();
    }

    @Override
    public void setRowIndex(final int rowIndex) {
        if (rowIndex < -1) {
            final String errMsg = "Illegal rowIndex " + rowIndex;
            throw new IllegalArgumentException(errMsg);
        }
        final int oldRowIndex = this.rowIndex.get();
        this.rowIndex.set(rowIndex);
        final DataModelListener[] listeners = getDataModelListeners();
        if ((listeners != null) && (listeners.length > 0)) {
            if ((getDataModel() != null) && (oldRowIndex != this.rowIndex.get())) {
                Object data = null;
                if (isRowAvailable()) {
                    data = getRowData();
                }
                final DataModelEvent event = new DataModelEvent(this, this.rowIndex.get(), data);
                for (DataModelListener listener : listeners) {
                    listener.rowSelected(event);
                }
            }
        }
    }

    @Override
    public Object getWrappedData() {
        return getDataModel();
    }

    @Override
    public void setWrappedData(final Object object) {
        setDataModel((DataModelDao) object);
        if (getDataModel() == null) {
            setRowIndex(-1);
        } else {
            setRowIndex(0);
        }

    }

    public DataModelDao getDataModel() {
        return dataModel;
    }

    public void setDataModel(final DataModelDao value) {
        this.dataModel = value;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getSelectCount() {
        return selectCount;
    }

    public void setSelectCount(String selectCount) {
        this.selectCount = selectCount;
    }

    public Map getSelectParam() {
        return selectParam;
    }

    public void setSelectParam(Map selectParam) {
        this.selectParam = selectParam;
    }
}
