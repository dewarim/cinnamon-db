package cinnamon

class CmnGroupUser  implements Serializable {

    static constraints = {
        cmnGroup unique: ['userAccount']
    }

    CmnGroup cmnGroup
    UserAccount userAccount

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof CmnGroupUser)) return false

        CmnGroupUser that = (CmnGroupUser) o

        if (cmnGroup != that.cmnGroup) return false
        if (userAccount != that.userAccount) return false

        return true
    }

    int hashCode() {
        int result
        result = (cmnGroup != null ? cmnGroup.hashCode() : 0)
        result = 31 * result + (userAccount != null ? userAccount.hashCode() : 0)
        return result
    }
}
