/**
 * @author Malte Neumann Created on 17.05.2013 at 13:57:55
 *
 */
package slash.navigation.converter.gui.actions;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import slash.navigation.base.BaseNavigationPosition;
import slash.navigation.converter.gui.models.PositionsModel;
import slash.navigation.gui.actions.FrameAction;


public class SortByTimeAction extends FrameAction implements Comparator<BaseNavigationPosition>{
    private final PositionsModel positionsModel;
    
    public SortByTimeAction(PositionsModel positionsModel){
        this.positionsModel = positionsModel;
    }
    
    @Override
    public void run() {
        List<BaseNavigationPosition> positions = this.positionsModel.getRoute().getPositions();
        Collections.sort(positions, this);
        this.positionsModel.fireTableRowsUpdated(0, positions.size()-1, 0);
    }

    @Override
    public int compare(BaseNavigationPosition o1, BaseNavigationPosition o2) {
        return Double.compare(o1.getTime().getTimeInMillis(), o2.getTime().getTimeInMillis());
    }

}
