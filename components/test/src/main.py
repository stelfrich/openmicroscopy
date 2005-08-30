# The script is executed just once by each worker
# process and defines the TestRunner class. The Grinder creates an
# instance of TestRunner for each worker thread, and repeatedly calls
# the instance for each run of that thread.

from java.lang import Throwable
from net.grinder.script.Grinder import grinder
from net.grinder.script import Test
from net.grinder.statistics import ExpressionView, StatisticsIndexMap, StatisticsView
from java.util import Set
from java.util import HashSet
from ome.itests import ComparisonUtils as Cmp
from ome.tests import HierarchyBrowsingTests as ITest
from ome.tests import OMEPerformanceData as Data
from ome.itests import OmeroGrinderTest as Omero
from ome.util  import Utils
from org.openmicroscopy.shoola.env.data.t import ShoolaGrinderTest as Shoola
from org.springframework.beans.factory.config import AutowireCapableBeanFactory as Autowire
from java.lang import Boolean as Bool


#################################################################
# SETUP
#
methods=Utils.getObjectVoidMethods(ITest) 
num=len(methods)

dummy = Omero("Data to be added").init() # To get context
ds = dummy.appContext.getBean("dataSource")

# Create a Test with a test number and a description. The test will be
# automatically registered with The Grinder console if you are using
# it.
oTests = [Test(i, "omero."+name+"()") for name, i in zip(methods, range(num))]
sTests = [Test(i+num, "shoola."+name+"()") for name, i in zip(methods, range(num))]

# A shorter alias for the grinder.logger.output() method and
# statistics
log = grinder.logger.output

# Our own statistics
szIndex = StatisticsIndexMap.getInstance().getLongIndex("userLong1")
szDetailView = StatisticsView()
szDetailView.add(ExpressionView("Size", "statistic.size", "userLong1"))
grinder.registerDetailStatisticsView(szDetailView)
grinder.registerSummaryStatisticsView(szDetailView) ## TODO THIS IS NOT ACCURATE!
#
#################################################################

# A TestRunner instance is created for each thread.
# It can be used to store thread-specific data.
class TestRunner:
   
    def __init__(self):
	self.percent =  0.050
	self.increase = 0.001
	self.run = 0
	self.omero = None
	self.shoola = None
	
    def getData(self):
	self.percent += self.increase # * self.run TODO
	self.run += 1
	data = Data(self.percent)
	data.setDataSource(ds)
	data.init()
	log(" ")
	log("========================================")
        log(str(data))
	log("========================================")
	log(" ")
	
	return data
            
    # This method is called for every run.
    def __call__(self):

	data = self.getData()
	_omero = Omero(data).init()
	_omero.setData(data) # Eeek.
	_shoola = Shoola(data)
        
   	which = data.randomChoice(10) # TODO
	grinder.statistics.delayReports = 1
	print(" Thread: "+str(grinder.threadID)+" Run: "+str(self.run)+"  Test: "+ str(which)+ " Percent: "+str(self.percent))
	for o,s in zip(oTests,sTests):
	#if (1):				#
	#	o=oTests[which]		# Replaces for o,s above
	#	s=sTests[which]		#
		self.omero = o.wrap(_omero)
		self.shoola = s.wrap(_shoola)
		a = self.doIt( o.description )
		b = self.doIt( s.description )

		#if not Cmp.compare(a,b): TODO
		#	log("xxxxxxxxxxxxxxxxxxxxxxxxxxx")
		#	log(o.description + " and " + s.description + "differ.")
		#	log("o="+str(a))
		#	log("s="+str(b))
		#	log("xxxxxxxxxxxxxxxxxxxxxxxxxxx")

    def doIt(self,method):
	success=1
	result=None
	try:
		result=eval("self."+method)
		sz = Utils.structureSize(result)
		#TODOUtils.writeXmlToFile(result,"log/"+method[:-2]+".run"+str(grinder.runNumber)+".thread"+str(grinder.threadID)+".xml")
		grinder.statistics.setValue(szIndex, sz)        
		log("Return from method "+method+" : ( "+str(sz)+")")
		log(str(result))
		log(" ")
	except Throwable, inst:
		result="Error occurred in "+method
		log("------------------------------------------")
		log(result+" :")
		t = inst
		while (t != None):
			log(str(t))
			next = inst.cause
			if t != next:
				t = next
			else:
				t = None		
		log(" ")
#		log("Stack Trace:")
#		for s in inst.stackTrace:
#			log(str(s))
		log("------------------------------------------")
		success=0
	grinder.statistics.setSuccess(success)
	grinder.statistics.report()
	return result

