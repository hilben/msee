# -*- encoding: utf-8 -*-

Gem::Specification.new do |s|
  s.name = "diff-lcs"
  s.version = "1.2.0"

  s.required_rubygems_version = Gem::Requirement.new(">= 0") if s.respond_to? :required_rubygems_version=
  s.authors = ["Austin Ziegler"]
  s.date = "2013-01-21"
  s.description = "Diff::LCS computes the difference between two Enumerable sequences using the\nMcIlroy-Hunt longest common subsequence (LCS) algorithm. It includes utilities\nto create a simple HTML diff output format and a standard diff-like tool.\n\nThis is release 1.2. Everyone is strongly encouraged to upgrade to this version\nas it fixes all known outstanding issues."
  s.email = ["austin@rubyforge.org"]
  s.executables = ["htmldiff", "ldiff"]
  s.extra_rdoc_files = ["Contributing.rdoc", "History.rdoc", "License.rdoc", "Manifest.txt", "README.rdoc", "docs/COPYING.txt", "docs/artistic.txt"]
  s.files = ["bin/htmldiff", "bin/ldiff", "Contributing.rdoc", "History.rdoc", "License.rdoc", "Manifest.txt", "README.rdoc", "docs/COPYING.txt", "docs/artistic.txt"]
  s.homepage = "http://diff-lcs.rubyforge.org/"
  s.rdoc_options = ["--main", "README.rdoc"]
  s.require_paths = ["lib"]
  s.rubyforge_project = "diff-lcs"
  s.rubygems_version = "1.8.23"
  s.summary = "Diff::LCS computes the difference between two Enumerable sequences using the McIlroy-Hunt longest common subsequence (LCS) algorithm"

  if s.respond_to? :specification_version then
    s.specification_version = 3

    if Gem::Version.new(Gem::VERSION) >= Gem::Version.new('1.2.0') then
      s.add_development_dependency(%q<rubyforge>, [">= 2.0.4"])
      s.add_development_dependency(%q<rdoc>, ["~> 3.10"])
      s.add_development_dependency(%q<rspec>, ["~> 2.0"])
      s.add_development_dependency(%q<rake>, ["~> 10.0"])
      s.add_development_dependency(%q<hoe-bundler>, ["~> 1.2.0"])
      s.add_development_dependency(%q<hoe-doofus>, ["~> 1.0.0"])
      s.add_development_dependency(%q<hoe-gemspec>, ["~> 1.0.0"])
      s.add_development_dependency(%q<hoe-git>, ["~> 1.5.0"])
      s.add_development_dependency(%q<hoe-rubygems>, ["~> 1.0.0"])
      s.add_development_dependency(%q<hoe-travis>, ["~> 1.2"])
      s.add_development_dependency(%q<hoe>, ["~> 3.4"])
    else
      s.add_dependency(%q<rubyforge>, [">= 2.0.4"])
      s.add_dependency(%q<rdoc>, ["~> 3.10"])
      s.add_dependency(%q<rspec>, ["~> 2.0"])
      s.add_dependency(%q<rake>, ["~> 10.0"])
      s.add_dependency(%q<hoe-bundler>, ["~> 1.2.0"])
      s.add_dependency(%q<hoe-doofus>, ["~> 1.0.0"])
      s.add_dependency(%q<hoe-gemspec>, ["~> 1.0.0"])
      s.add_dependency(%q<hoe-git>, ["~> 1.5.0"])
      s.add_dependency(%q<hoe-rubygems>, ["~> 1.0.0"])
      s.add_dependency(%q<hoe-travis>, ["~> 1.2"])
      s.add_dependency(%q<hoe>, ["~> 3.4"])
    end
  else
    s.add_dependency(%q<rubyforge>, [">= 2.0.4"])
    s.add_dependency(%q<rdoc>, ["~> 3.10"])
    s.add_dependency(%q<rspec>, ["~> 2.0"])
    s.add_dependency(%q<rake>, ["~> 10.0"])
    s.add_dependency(%q<hoe-bundler>, ["~> 1.2.0"])
    s.add_dependency(%q<hoe-doofus>, ["~> 1.0.0"])
    s.add_dependency(%q<hoe-gemspec>, ["~> 1.0.0"])
    s.add_dependency(%q<hoe-git>, ["~> 1.5.0"])
    s.add_dependency(%q<hoe-rubygems>, ["~> 1.0.0"])
    s.add_dependency(%q<hoe-travis>, ["~> 1.2"])
    s.add_dependency(%q<hoe>, ["~> 3.4"])
  end
end