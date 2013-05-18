/**
 * @author Malte Neumann Created on 18.05.2013 at 18:34:22
 *
 */
package slash.navigation.converter.gui.actions;

import javax.swing.JTable;

import slash.navigation.base.BaseRoute;
import slash.navigation.converter.gui.models.FormatAndRoutesModel;
import slash.navigation.converter.gui.models.PositionsModel;
import slash.navigation.gui.actions.FrameAction;


public class MergeRoutesAction extends FrameAction {
    private final FormatAndRoutesModel formatAndRoutesModel;
    private final PositionsModel positionsModel;
    
    public MergeRoutesAction(PositionsModel positionsModel, FormatAndRoutesModel formatAndRoutesModel){
        this.formatAndRoutesModel = formatAndRoutesModel;
        this.positionsModel = positionsModel;
    }
    
    @Override
    public void run() {
        if (this.formatAndRoutesModel.getSize() > 1){
            BaseRoute routeToInsert = this.formatAndRoutesModel.getRoute(0);
            while (this.formatAndRoutesModel.getRoutes().size() > 1) {
                BaseRoute route = this.formatAndRoutesModel.getRoutes().get(1);
                routeToInsert.getPositions().addAll(route.getPositions());
                this.formatAndRoutesModel.removePositionList(route);
            }
        }
        this.positionsModel.fireTableRowsUpdated(0, Integer.MAX_VALUE, 0);
    }

}
