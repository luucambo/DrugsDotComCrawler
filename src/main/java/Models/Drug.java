package Models;

import java.util.ArrayList;
import java.util.List;

public class Drug
{
    private String genericName ;
    private String overview;
    private List<String> branchNames ;
    private String url;

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public List<String> getBranchNames() {
        return branchNames;
    }

    public void setBranchNames(List<String> branchNames) {
        this.branchNames = branchNames;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}