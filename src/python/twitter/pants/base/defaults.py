from collections import namedtuple


PantsOption = namedtuple('PantsOption', 'section option valtype default')


class Defaults(object):
  DEFAULT_PANTS_WORKDIR = PantsOption(section='DEFAULT',
                                      option='pants_workdir',
                                      valtype=str,
                                      default=None)
  """pants_workdir is automatically set by pants."""

  REPORTING_REPORTS_DIR = PantsOption(section='reporting',
                                      option='reports_dir',
                                      valtype=str,
                                      default='reports')
  """Directory, relative to pants_workdir, where reports will be generated."""
