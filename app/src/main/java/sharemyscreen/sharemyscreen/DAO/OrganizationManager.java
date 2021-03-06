package sharemyscreen.sharemyscreen.DAO;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.Objects;

import sharemyscreen.sharemyscreen.Entities.OrganizationEntity;
import sharemyscreen.sharemyscreen.Entities.ProfileEntity;
import sharemyscreen.sharemyscreen.Entities.RoomEntity;

/**
 * Created by roucou_c on 20/06/2016.
 */
public class OrganizationManager extends OrganizationDAO {
    private final ProfileManager _profileManager;
    private final OrganizationByProfileManager _organizationByProfileManager;

    public OrganizationManager(SQLiteDatabase mDb, ProfileManager profileManager) {
        super(mDb);
        _profileManager = profileManager;
        _organizationByProfileManager = new OrganizationByProfileManager(mDb, profileManager);
    }


    public long add(OrganizationEntity organizationEntityNew) {
        
        if (organizationEntityNew.get_creator() != null && organizationEntityNew.get_creator_public_id() == null) {
            organizationEntityNew.set_creator_public_id(organizationEntityNew.get_creator().get_public_id());
        }

        if (organizationEntityNew.get_owner() != null && organizationEntityNew.get_owner_public_id() == null) {
            organizationEntityNew.set_owner_public_id(organizationEntityNew.get_owner().get_public_id());
        }

        if (organizationEntityNew !=  null && organizationEntityNew.get_public_id() != null) {
            
            OrganizationEntity organizationEntity = this.selectByPublic_id(organizationEntityNew.get_public_id());

            _organizationByProfileManager.add(organizationEntityNew);

            if (organizationEntity != null) {
                organizationEntityNew.set_id(organizationEntity.get_id());
                super.modify(organizationEntityNew);
                return organizationEntity.get_id();
            }
            else {
                return super.add(organizationEntityNew);
            }
        }
        return 0;

    }

    @Override
    public OrganizationEntity selectByPublic_id(String public_id) {
        OrganizationEntity organizationEntity = super.selectByPublic_id(public_id);

        if (organizationEntity != null) {
            organizationEntity.set_creator(_profileManager.selectByPublic_id(organizationEntity.get_creator_public_id()));
            organizationEntity.set_owner(_profileManager.selectByPublic_id(organizationEntity.get_owner_public_id()));
            organizationEntity.set_members(_organizationByProfileManager.selectProfilesByOrganization(public_id));
        }

        return organizationEntity;
    }

    public void add(List<OrganizationEntity> organizationEntityList) {
        if (organizationEntityList != null) {
            for (OrganizationEntity organizationEntity : organizationEntityList) {
                long organization_id = this.add(organizationEntity);
            }
        }
    }

    public List<OrganizationEntity> selectAllByProfile_id(String profile_public_id) {
        return _organizationByProfileManager.selectAllByProfile_id(profile_public_id);
    }

    public boolean isOwner(String organization_public_id, String profile_public_id) {
        OrganizationEntity organizationEntity = this.selectByPublic_id(organization_public_id);
        if (organizationEntity != null && Objects.equals(organizationEntity.get_owner_public_id(), profile_public_id)) {
            return true;
        }
        return false;
    }

    public void delete(String organization_public_id) {
        _organizationByProfileManager.deleteByOrganization(organization_public_id);
        super.delete(organization_public_id);
    }
}
