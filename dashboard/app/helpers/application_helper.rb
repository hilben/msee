module ApplicationHelper
    def get_version
        # repo = Grit::Repo.new('.')
        # repo.commits("origin/master").first.id[0,12]
        ""
    end

    def get_last_change_date
        # repo = Grit::Repo.new('.')
        # repo.commits("origin/master").first.authored_date
        ""
    end
end
