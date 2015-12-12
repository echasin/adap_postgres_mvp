/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.innvo.domain.Asset;
import java.util.List;

/**
 *
 * @author echasin
 */
public class AssetUtil {

    private int recordsFiltered;
    private int recordsTotal;
    private int draw;
    private List<Asset> list;

    /**
     * @return the recordsFiltered
     */
    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    /**
     * @param recordsFiltered the recordsFiltered to set
     */
    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    /**
     * @return the recordsTotal
     */
    public int getRecordsTotal() {
        return recordsTotal;
    }

    /**
     * @param recordsTotal the recordsTotal to set
     */
    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    /**
     * @return the draw
     */
    public int getDraw() {
        return draw;
    }

    /**
     * @param draw the draw to set
     */
    public void setDraw(int draw) {
        this.draw = draw;
    }

    /**
     * @return the list
     */
    public List<Asset> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<Asset> list) {
        this.list = list;
    }

}