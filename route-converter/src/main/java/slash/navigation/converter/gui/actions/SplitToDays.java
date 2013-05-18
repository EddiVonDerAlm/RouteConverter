/**
 * @author Malte Neumann Created on 18.05.2013 at 11:40:20
 *
 */
package slash.navigation.converter.gui.actions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JTable;

import slash.common.type.CompactCalendar;
import slash.navigation.base.BaseNavigationFormat;
import slash.navigation.base.BaseNavigationPosition;
import slash.navigation.base.BaseRoute;
import slash.navigation.converter.gui.models.FormatAndRoutesModel;
import slash.navigation.converter.gui.models.PositionsModel;
import slash.navigation.gui.actions.FrameAction;


public class SplitToDays extends FrameAction {
    private final JTable table;
    private final PositionsModel positionsModel;
    private final FormatAndRoutesModel formatAndRoutesModel;
    private final SplitPositionListAction splitAction;

    public SplitToDays(JTable table, PositionsModel positionsModel, FormatAndRoutesModel formatAndRoutesModel,
                                             SplitPositionListAction splitAction) {
        this.table = table;
        this.positionsModel = positionsModel;
        this.formatAndRoutesModel = formatAndRoutesModel;
        this.splitAction = splitAction;
    }
    
    private boolean isSameDay(CompactCalendar date1, CompactCalendar date2){
        return (date1 != null) && (date2 != null) &&
               (date1.getCalendar().get(Calendar.YEAR) == 
                    date2.getCalendar().get(Calendar.YEAR)) &&
               (date1.getCalendar().get(Calendar.DAY_OF_YEAR) == 
                   date2.getCalendar().get(Calendar.DAY_OF_YEAR));
    }
    
    private List<Integer> getStartOfDayIndizes(){
        List<Integer> startDayList = new ArrayList<Integer>();
        List<BaseNavigationPosition> positionsList = this.formatAndRoutesModel
                .getSelectedRoute().getPositions();
        CompactCalendar lastStartDay = null;
        for (int i = 0; i < positionsList.size(); i++){
            BaseNavigationPosition position = positionsList.get(i);
            if (! isSameDay(lastStartDay, position.getTime())){
                startDayList.add(i);
                lastStartDay = position.getTime();
            }
        }
        return startDayList;
    }
    
    private void selectTableRows(List<Integer> rowsToSelect){
        this.table.clearSelection();
        for (Integer index: rowsToSelect){
            if (index > 0)
                this.table.getSelectionModel().addSelectionInterval(index.intValue(), index.intValue());
        }
    }
    
    private String createRouteName(BaseRoute<BaseNavigationPosition, BaseNavigationFormat> route){
        String name = route.getName();
        if (route.getPositionCount() > 0){
            BaseNavigationPosition firstPosition = route.getPosition(0);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd (EEEE)");
            name = dateFormat.format(firstPosition.getTime().getCalendar().getTime());
        }
        return name;
    }
    
    private void renameRoutes(){
        List<BaseRoute> routes = this.formatAndRoutesModel.getRoutes();
        for (BaseRoute route: routes){
            route.setName(createRouteName(route));
        }
    }
    
    @Override
    public void run() {
        List<Integer> startDaysIndizes = getStartOfDayIndizes();
        selectTableRows(startDaysIndizes);
        this.splitAction.run();
        renameRoutes();
        this.positionsModel.fireTableRowsUpdated(0, Integer.MAX_VALUE, 0);
    }

}
