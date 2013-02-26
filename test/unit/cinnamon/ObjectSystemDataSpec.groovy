package cinnamon

import grails.plugin.spock.UnitSpec
import grails.test.mixin.Mock

/**
 * Unit test for OSDs.
 */
@Mock([ObjectSystemData])
class ObjectSystemDataSpec extends UnitSpec{

    def 'test fixLatestHeadAndBranch: v1: one object'() {
        setup:
        def v1 = new ObjectSystemData(cmnVersion: '1')

        when:
        v1.fixLatestHeadAndBranch([])

        then:
        v1.latestHead
        v1.latestBranch
    }

    def 'test fixLatestHeadAndBranch: v2: one object with predecessor'() {
        setup:
        def v1 = new ObjectSystemData(cmnVersion: '1')
        def v2 = new ObjectSystemData(cmnVersion: '2')
        v2.predecessor = v1

        when:
        v2.fixLatestHeadAndBranch([])

        then:
        !v1.latestHead
        !v1.latestBranch
        v2.latestHead
        v2.latestBranch
    }


    def 'test fixLatestHeadAndBranch: v3: one object with branched predecessor'() {
        setup:
        def v1 = new ObjectSystemData(cmnVersion: '1')
        def v1Branch = new ObjectSystemData(cmnVersion: '1-1.1', predecessor: v1)
        def v2 = new ObjectSystemData(cmnVersion: '2')
        v2.predecessor = v1

        when:
        v1.fixLatestHeadAndBranch([v1Branch,v2])
        v1Branch.fixLatestHeadAndBranch([])
        v2.fixLatestHeadAndBranch([])

        then:
        !v1.latestHead
        !v1.latestBranch
        v2.latestHead
        v2.latestBranch
        v1Branch.latestBranch
        !v1Branch.latestHead
    }

    def 'test fixLatestHeadAndBranch: v4: one object in head with branched child'() {
        setup:
        def v1 = new ObjectSystemData(cmnVersion: '1')
        def v1Branch = new ObjectSystemData(cmnVersion: '1-1.1', predecessor: v1)

        when:
        v1.fixLatestHeadAndBranch([v1Branch])

        then:
        v1.latestHead
        !v1.latestBranch
        v1Branch.latestBranch
        !v1Branch.latestHead
    }

    def 'test fixLatestHeadAndBranch: v5: two object in head, with branched child'() {
        setup:
        def v1 = new ObjectSystemData(cmnVersion: '1')
        def v2 = new ObjectSystemData(cmnVersion: '2', predecessor: v1)
        def v3 = new ObjectSystemData(cmnVersion: '3', predecessor: v2)
        def v2Branch = new ObjectSystemData(cmnVersion: '2.2-1', predecessor: v2)

        when:
        v2.fixLatestHeadAndBranch([v2Branch, v3])
        v3.fixLatestHeadAndBranch([])

        then:
        v3.latestHead
        v3.latestBranch
        ! v2.latestHead
        ! v2.latestBranch
        ! v2Branch.latestHead
        v2Branch.latestBranch
    }
}

